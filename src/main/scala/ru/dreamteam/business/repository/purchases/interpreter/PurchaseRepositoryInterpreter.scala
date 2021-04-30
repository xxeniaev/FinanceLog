package ru.dreamteam.business.repository.purchases.interpreter

import cats.Monad
import cats.syntax.all._
import cats.effect.BracketThrow
import doobie.ConnectionIO
import doobie.h2.H2Transactor
import ru.dreamteam.business.{Currency, Money, Purchase, User}
import doobie.implicits._
import ru.dreamteam.business.Purchase.PurchaseCategory
import ru.dreamteam.business.repository.purchases.PurchasesRepository
import ru.dreamteam.business.repository.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.repository.purchases.interpreter.PurchaseRepositoryInterpreter.{insertPurchase, selectByCategory, selectByPurchaseId, selectByUserId, transform}

class PurchaseRepositoryInterpreter[F[_]: BracketThrow: Monad](transactor: H2Transactor[F])
  extends PurchasesRepository[F] {

  override def findByUserId(userId: User.Id): F[List[Purchase]] = for {
    raw   <- selectByUserId(userId.id).transact(transactor)
    result = raw.flatMap(transform)
  } yield result

  override def findByCategory(userId: User.Id, category: PurchaseCategory): F[List[Purchase]] =
    for {
      raw   <- selectByCategory(userId.id, category).transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  override def findByPurchaseId(userId: User.Id, purchaseId: Purchase.Id): F[Option[Purchase]] =
    for {
      raw   <- selectByPurchaseId(userId.id, purchaseId.id).transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  override def addPurchase(userId: User.Id, purchase: PurchaseRequest): F[Purchase.Id] = for {
    id <- insertPurchase(
            userId.id,
            purchase.money.amount,
            purchase.money.currency.entryName,
            purchase.comment.comment,
            purchase.category.entryName
          ).transact(transactor)
  } yield Purchase.Id(id)

}

object PurchaseRepositoryInterpreter {

  def transform(raw: PurchaseRaw): Option[Purchase] = for {
    purchasesType <- PurchaseCategory.withNameInsensitiveOption(raw.category)
    currency       = Currency.parse(raw.currency)
  } yield Purchase(Purchase.Id(raw.purchaseId), Money(raw.amount, currency), Purchase.Comment(raw.comment), purchasesType)

  // PurchaseType.withNameInsensitiveOption(raw.category).map(type => {
  // val c = currency
  // Purchase(???)
  // })
  def selectByUserId(userId: Int): ConnectionIO[List[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE userId = $userId"
      .query[PurchaseRaw]
      .to[List]

  def selectByCategory(
    userId: Int,
    category: PurchaseCategory
  ): doobie.ConnectionIO[List[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE category = ${category.entryName} AND userId = $userId"
      .query[PurchaseRaw]
      .to[List]

  def selectByPurchaseId(userId: Int, purchaseId: Int): ConnectionIO[Option[PurchaseRaw]] =
    sql"SELECT purchaseId, money, comment, category FROM purchases WHERE purchaseId = $purchaseId AND userId = $userId"
      .query[PurchaseRaw]
      .option

  def insertPurchase(
    userId: Int,
    amount: BigDecimal,
    currency: String,
    comment: String,
    category: String
  ): ConnectionIO[Int] =
    sql"INSERT INTO purchases (amount, currency, comment, category, userId) VALUES ($amount, $currency, $comment, $category, $userId)"
      .update
      .withUniqueGeneratedKeys[Int]("purchaseId")

  case class PurchaseRaw(
    purchaseId: Int,
    amount: BigDecimal,
    currency: String,
    comment: String,
    category: String,
    userId: Int
  )

}
