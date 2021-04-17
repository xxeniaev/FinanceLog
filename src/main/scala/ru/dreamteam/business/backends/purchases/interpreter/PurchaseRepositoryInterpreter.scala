package ru.dreamteam.business.backends.purchases.interpreter

import cats.Monad
import cats.syntax.all._
import cats.effect.BracketThrow
import doobie.h2.H2Transactor
import ru.dreamteam.business.{Money, Purchase, User}
import doobie.implicits._
import ru.dreamteam.business.backends.purchases.PurchasesRepository
import ru.dreamteam.business.backends.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.backends.purchases.interpreter.PurchaseRepositoryInterpreter.{PurchaseRaw, selectPurchase, transform}


class PurchaseRepositoryInterpreter[F[_]: BracketThrow: Monad](transactor: H2Transactor[F]) extends PurchasesRepository[F] {
  // 0. no implicits found for parameter ev: Bracket[F, Throwable] это что вообще и как исправить
  // кажется, надо что-то типа строчки ниже, но я не понимаю зачем и как
  //  implicit val ev: Bracket[F, Throwable] = Bracket[F, Throwable]


  override def getAllPurchases(userId: User.Id): F[List[Purchase]] = {
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE userId = $userId"
      .query[PurchaseRaw]
      .to[List]
      .transact(transactor)
    ???
  }

  override def getPurchasesByCategory(userId: User.Id, category: Purchase.Category): F[List[Purchase]] = {
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE category = ${category.category.entryName} AND userId = $userId"
      .query[PurchaseRaw]
      .to[List]
      .transact(transactor)
    ???
  }

  // 1. переделать без Either

  override def findOnePurchase(userId: User.Id, purchaseId: Purchase.Id): F[Option[Purchase]] = {
    for {
      raw <- selectPurchase(purchaseId.id, userId.id).transact(transactor)
      res = raw.flatMap(transform)
    } yield res
  }

  // 2. тут вопросы по поводу создания id
  override def addPurchase(userId: User.Id, purchase: PurchaseRequest): F[Purchase] = {
    // нужно переписать
//    sql"INSERT INTO purchases (money, comment, category, userId) VALUES (${purchase.money}, ${purchase.comment}, ${purchase.category.category.entryName}, ${userId})"
//      .update
//      .withUniqueGeneratedKeys[Long]("purchaseId")
//      .transact(transactor).map { id =>
//      purchase.copy(id)
//    }
    ???
  }
}

object PurchaseRepositoryInterpreter {

  def transform(raw: PurchaseRaw): Option[Purchase] = ???


  def selectPurchase(pId: String, userId: String): doobie.ConnectionIO[Option[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE purchaseId = $pId AND userId = $userId"
    .query[PurchaseRaw]
    .option

  case class PurchaseRaw(
                          purchaseId: Long,
                          money: BigDecimal,
                          comment: String,
                          category: String,
                          currency: String
                        )

}
