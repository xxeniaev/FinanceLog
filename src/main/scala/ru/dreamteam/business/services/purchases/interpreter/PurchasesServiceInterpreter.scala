package ru.dreamteam.business.services.purchases.interpreter

import cats.Monad
import cats.effect.Sync
import cats.syntax.all._
import ru.dreamteam.business.Purchase.{Comment, PurchaseCategory}
import ru.dreamteam.business.repository.purchases.PurchasesRepository
import ru.dreamteam.business.repository.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.services.users.interpreter.BusinessError
import ru.dreamteam.business.{Money, Purchase, User}

class PurchasesServiceInterpreter[F[_]: Sync: Monad](repo: PurchasesRepository[F]) extends PurchasesService[F] {
  override def getPurchases(userId: User.Id): F[List[Purchase]] = repo.findByUserId(userId)

  override def getPurchaseByType(
    userId: User.Id,
    purchaseCategory: PurchaseCategory
  ): F[List[Purchase]] = repo.findByCategory(userId, purchaseCategory)

  override def addPurchase(
    userId: User.Id,
    money: Money,
    comment: String,
    purchaseCategory: PurchaseCategory
  ): F[Purchase] = for {
    purchaseId <- repo.addPurchase(userId, PurchaseRequest(money, Comment(comment), purchaseCategory))
    purchaseOption <- repo.findByPurchaseId(userId, purchaseId)
    purchase <- Sync[F].fromOption(purchaseOption, PurchaseNotExists("purchase not found"))
  } yield purchase

  override def purchaseInfo(userId: User.Id, purchaseId: Purchase.Id): F[String] = ???
}

case class PurchaseNotExists(msg: String) extends BusinessError(msg)