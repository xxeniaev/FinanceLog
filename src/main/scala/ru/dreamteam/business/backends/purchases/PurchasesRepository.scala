package ru.dreamteam.business.backends.purchases

import ru.dreamteam.business.backends.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.{Money, Purchase, User}


trait PurchasesRepository[F[_]] {
  def getAllPurchases(userId: User.Id): F[List[Purchase]]
  def getPurchasesByCategory(userId: User.Id, category: Purchase.Category): F[List[Purchase]]
  def findOnePurchase(userId: User.Id, purchaseId: Purchase.Id): F[Option[Purchase]]
  def addPurchase(userId: User.Id, purchase: PurchaseRequest): F[Purchase]
}

object PurchasesRepository {
  case class PurchaseRequest(
                              money: Money,
                              comment: Purchase.Comment,
                              category: Purchase.Category
                            )
}