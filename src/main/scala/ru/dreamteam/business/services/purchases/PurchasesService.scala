package ru.dreamteam.business.services.purchases

import ru.dreamteam.business.Purchase.PurchaseType
import ru.dreamteam.business.{Money, Purchase, User}

trait PurchasesService[F[_]] {

  // дописать создать, все на ваше усмотрение
  def getPurchase(userId: User.Id): F[List[Purchase]]

  def getPurchaseByType(userId: User.Id, purchaseType: PurchaseType): F[List[Purchase]]

  def addPurchase(userId: User.Id, money: Money, comment: String, `type`: PurchaseType)
}
