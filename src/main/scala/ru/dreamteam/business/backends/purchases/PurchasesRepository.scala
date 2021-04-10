package ru.dreamteam.business.backends.purchases

import doobie.Meta
import ru.dreamteam.business.{Purchase, PurchaseNotFoundError}
import cats.effect.IO
import doobie.util.transactor.Transactor
import fs2.Stream
import doobie._
import doobie.implicits._
import doobie.util.transactor
import ru.dreamteam.database.H2App

import scala.concurrent.Future

trait PurchasesRepository[F[_]] {
  def getAllPurchases: F[List[Purchase]]
  def getSomePurchase(category: Purchase.PurchaseType): F[List[Purchase]]
  def getOnePurchase(id: Purchase.Id): F[Purchase]
  def addPurchase(purchase: Purchase)
}

class PurchaseRepositoryInterpreter[F[_]]() extends PurchasesRepository[F] {
  val transactor = H2App.transactor

  override def getAllPurchases: Future[List[Purchase]] = {
    sql"SELECT money, comment, category FROM purchase".query[Purchase].stream.transact(transactor)
    // в
  }

  override def getSomePurchases(category: Purchase.PurchaseType): Future[List[Purchase]] = {
    sql"SELECT * FROM purchase WHERE category = $category".query[Purchase].stream.transact(transactor)
  }

  override def getOnePurchase(id: Purchase.Id): Future[Either[PurchaseNotFoundError.type, Purchase]] = {
    sql"SELECT * FROM purchase WHERE id = $id".query[Purchase].option.transact(transactor).map {
      case Some(purchase) => Right(purchase)
      case None => Left(PurchaseNotFoundError)
    }
  }

  override def addPurchase(purchase: Purchase): Future[Purchase] = {
    sql"INSERT INTO purchase (money, comment, category) VALUES (${purchase.money}, ${purchase.comment}, ${purchase.category})".update.withUniqueGeneratedKeys[Long]("id").transact(transactor).map { id =>
      purchase.copy(id = Some(id))
    }
  }
}

