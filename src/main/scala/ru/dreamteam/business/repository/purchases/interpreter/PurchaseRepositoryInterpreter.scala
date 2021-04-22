package ru.dreamteam.business.repository.purchases.interpreter

import cats.Monad
import cats.syntax.all._
import cats.effect.BracketThrow
import doobie.h2.H2Transactor
import ru.dreamteam.business.{Currency, Money, Purchase, User}
import doobie.implicits._
import ru.dreamteam.business.Purchase.PurchaseType
import ru.dreamteam.business.repository.purchases.PurchasesRepository
import ru.dreamteam.business.repository.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.repository.purchases.interpreter.PurchaseRepositoryInterpreter.{insertPurchase, selectByCategory, selectByPurchaseId, selectByUserId, transform}

class PurchaseRepositoryInterpreter[F[_]: BracketThrow: Monad](transactor: H2Transactor[F])
  extends PurchasesRepository[F] {

  override def findByUserId(userId: User.Id): F[List[Purchase]] = for {
    raw   <- selectByUserId(userId.id).transact(transactor)
    result = raw.flatMap(transform)
  } yield result

  override def findByCategory(userId: User.Id, category: PurchaseType): F[List[Purchase]] = for {
    raw   <- selectByCategory(userId.id, category).transact(transactor)
    result = raw.flatMap(transform)
  } yield result

  override def findByPurchaseId(userId: User.Id, purchaseId: Purchase.Id): F[Option[Purchase]] =
    for {
      raw   <- selectByPurchaseId(userId.id, purchaseId.id).transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  override def addPurchase(userId: User.Id, purchase: PurchaseRequest): F[Purchase.Id] = for {
    id <- insertPurchase(userId.id, purchase: PurchaseRequest).transact(transactor)
  } yield Purchase.Id(id)

}

object PurchaseRepositoryInterpreter {

  def transform(raw: PurchaseRaw): Option[Purchase] = for {
    purchasesType <- PurchaseType.withNameInsensitiveOption(raw.category)
    currency       = Currency.parse(raw.currency)
  } yield Purchase(Purchase.Id(raw.purchaseId), Money(raw.amount, currency), Purchase.Comment(raw.comment), purchasesType)

  // PurchaseType.withNameInsensitiveOption(raw.category).map(type => {
  // val c = currency
  // Purchase(???)
  // })
  def selectByUserId(userId: String): doobie.ConnectionIO[List[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE userId = $userId"
      .query[PurchaseRaw]
      .to[List]

  def selectByCategory(
    userId: String,
    category: PurchaseType
  ): doobie.ConnectionIO[List[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE category = ${category.entryName} AND userId = $userId"
      .query[PurchaseRaw]
      .to[List]

  def selectByPurchaseId(userId: String, pId: String): doobie.ConnectionIO[Option[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE purchaseId = $pId AND userId = $userId"
      .query[PurchaseRaw]
      .option

  def insertPurchase(userId: String, purchase: PurchaseRequest): doobie.ConnectionIO[String] = ???
//    sql"INSERT INTO purchases (amount, currency, comment, category, userId) VALUES (${purchase.money.amount}, ${purchase.money.currency}, ${purchase.comment}, ${purchase.category.entryName}, $userId)"
//      .update
//      .withUniqueGeneratedKeys[String]("purchaseId")

  case class PurchaseRaw(
    purchaseId: String,
    amount: BigDecimal,
    currency: String,
    comment: String,
    category: String
  )

}
