package ru.dreamteam.business.backends.purchases.interpreter

import cats.Monad
import cats.syntax.all._
import cats.effect.BracketThrow
import doobie.h2.H2Transactor
import ru.dreamteam.business.{Money, Purchase, User}
import doobie.implicits._
import ru.dreamteam.business.Purchase.{Category, PurchaseType}
import ru.dreamteam.business.backends.purchases.PurchasesRepository
import ru.dreamteam.business.backends.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.backends.purchases.interpreter.PurchaseRepositoryInterpreter.{add, selectByCategory, selectByPurchaseId, selectByUserId, transform}
import ru.dreamteam.business.backends.users.interpreter.UsersRepositoryInterpreter.add


class PurchaseRepositoryInterpreter[F[_]: BracketThrow: Monad](transactor: H2Transactor[F]) extends PurchasesRepository[F] {
  override def findByUserId(userId: User.Id): F[List[Purchase]] =
    for {
      raw <- selectByUserId(userId.id).transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  override def findByCategory(userId: User.Id, category: Purchase.Category): F[List[Purchase]] =
    for {
      raw <- selectByCategory(userId.id, category.category).transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  override def findByPurchaseId(userId: User.Id, purchaseId: Purchase.Id): F[Option[Purchase]] =
    for {
      raw <- selectByPurchaseId(userId.id, purchaseId.id).transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  // методом add получаем строку - id покупки -> делаем Id из строки -> возвращаем его
  // транзакт красненький (!) - что-то с ним сделать
  override def addPurchase(userId: User.Id, purchase: PurchaseRequest): F[Purchase] = {
    for {
      raw <- add(userId.id, purchase.money, purchase.comment, purchase.category).transact(transactor).map { id => purchase.copy(id) }
      id <- add(userId.id, purchase.money, purchase.comment, purchase.category)
      result = Purchase.Id(id.toString)
    } yield result
//    sql"INSERT INTO purchases (money, comment, category, userId) VALUES (${purchase.money}, ${purchase.comment}, ${purchase.category.category.entryName}, ${userId})"
//      .update
//      .withUniqueGeneratedKeys[Long]("purchaseId")
//      .transact(transactor).map { id =>
//      purchase.copy(id)
//    }
  }
}

object PurchaseRepositoryInterpreter {
  // написать метод(после разборок с таким же методом у юзера)
  def transform(raw: PurchaseRaw): Option[Purchase] = ???

  def selectByUserId(userId: String): doobie.ConnectionIO[List[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE userId = $userId"
      .query[PurchaseRaw]
      .to[List]

  def selectByCategory(userId: String, category: PurchaseType): doobie.ConnectionIO[List[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE category = ${category.entryName} AND userId = $userId"
      .query[PurchaseRaw]
      .to[List]

  def selectByPurchaseId(userId: String, pId: String): doobie.ConnectionIO[Option[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE purchaseId = $pId AND userId = $userId"
    .query[PurchaseRaw]
    .option

  // тоже дописать метод
   def add(userId: String, money: String, comment: String, category: String):  String = ???

  case class PurchaseRaw(
                          purchaseId: Long,
                          money: BigDecimal,
                          comment: String,
                          category: String,
                          currency: String
                        )

}
