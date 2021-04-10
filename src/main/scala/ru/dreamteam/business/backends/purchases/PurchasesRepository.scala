package ru.dreamteam.business.backends.purchases

import ru.dreamteam.business.{Purchase, User}


trait PurchasesRepository[F[_]] {
  def getAllPurchases(userId: User.Id): F[List[Purchase]]
  def getSomePurchase(userId: User.Id, category: Purchase.PurchaseType): F[List[Purchase]]
  def getOnePurchase(userId: User.Id, purchaseId: Purchase.Id): F[Purchase]
  def addPurchase(userId: User.Id, purchase: Purchase)
}