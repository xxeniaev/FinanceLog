package ru.dreamteam.business.backends.purchases.interpreter

import doobie.h2.H2Transactor
import ru.dreamteam.business.{Purchase, PurchaseNotFoundError, User}
import doobie.implicits._
import ru.dreamteam.business.backends.purchases.PurchasesRepository

import scala.concurrent.Future

class PurchaseRepositoryInterpreter[F[_]](transactor: H2Transactor[F]) extends PurchasesRepository[F] {
  // 0. no implicits found for parameter ev: Bracket[F, Throwable] это что вообще и как исправить
  // кажется, надо что-то типа строчки ниже, но я не понимаю зачем и как
//  implicit val ev: Bracket[F, Throwable] = Bracket[F, Throwable]


  override def getAllPurchases(userId: User.Id): F[List[Purchase]] = {
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE userId = $userId".query[Purchase].to[List].transact(transactor)
  }

  override def getSomePurchases(userId: User.Id, category: Purchase.PurchaseType): F[List[Purchase]] = {
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE category = $category AND userId = $userId".query[Purchase].to[List].transact(transactor)
  }

  // 1. переделать без Either

  override def getOnePurchase(userId: User.Id, purchaseId: Purchase.Id): F[Purchase] = {
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE purchaseId = $purchaseId AND userId = $userId".query[Purchase].option.transact(transactor).map {
      case Some(purchase) => Right(purchase)
      case None => Left(PurchaseNotFoundError)
    }
  }

  // 2. тут вопросы по поводу создания id
  override def addPurchase(userId: User.Id, purchase: Purchase): Future[Purchase] = {
    sql"INSERT INTO purchases (money, comment, category, userId) VALUES (${purchase.money}, ${purchase.comment}, ${purchase.category}, ${userId})".update.withUniqueGeneratedKeys[Long]("purchaseId").transact(transactor).map { id =>
      purchase.copy(id)
    }
  }
}

