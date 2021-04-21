package ru.dreamteam.business.services.purchases

import ru.dreamteam.business.Purchase.PurchaseType
import ru.dreamteam.business.{Money, Purchase, User}

trait PurchasesService[F[_]] {

  // дописать создать, все на ваше усмотрение
  /* Д: пока решили остановиться на этих методах, потом можно добавить выдачу покупок за какой-нибудь промежуток времени
  или вообще комбинацию некоторых методов: выдать за определенный промежуток определенного типа или выдача сразу
  нескольких типов
  */
  def getPurchase(userId: User.Id): F[List[Purchase]]

  def getPurchaseByType(userId: User.Id, purchaseType: PurchaseType): F[List[Purchase]]

  def addPurchase(userId: User.Id, money: Money, comment: String, `type`: PurchaseType)
}
