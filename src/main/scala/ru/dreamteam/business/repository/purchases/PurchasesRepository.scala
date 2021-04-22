package ru.dreamteam.business.repository.purchases

import ru.dreamteam.business.Purchase.PurchaseType
import ru.dreamteam.business.repository.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.{Money, Purchase, User}


trait PurchasesRepository[F[_]] {
  def findByUserId(userId: User.Id): F[List[Purchase]]
  def findByCategory(userId: User.Id, category: PurchaseType): F[List[Purchase]]
  def findByPurchaseId(userId: User.Id, purchaseId: Purchase.Id): F[Option[Purchase]]
  def addPurchase(userId: User.Id, purchase: PurchaseRequest): F[Purchase.Id]
}

object PurchasesRepository {
  case class PurchaseRequest(
                              money: Money,
                              comment: Purchase.Comment,
                              category: PurchaseType
                            )
}