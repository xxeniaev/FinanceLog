package ru.dreamteam.business.services.purchases

import ru.dreamteam.business.Purchase.PurchaseCategory
import ru.dreamteam.business.{Money, Purchase, User}

trait PurchasesService[F[_]] {

  def getPurchases(userId: User.Id): F[List[Purchase]]

  def getPurchaseByType(userId: User.Id, purchaseCategory: PurchaseCategory): F[List[Purchase]]

  def addPurchase(
    userId: User.Id,
    money: Money,
    comment: String,
    purchaseCategory: PurchaseCategory
  ): F[Purchase.Id]

  def purchaseInfo(userId: User.Id, purchaseId: Purchase.Id): F[String]

}
