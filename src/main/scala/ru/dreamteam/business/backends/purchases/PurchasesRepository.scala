package ru.dreamteam.business.backends.purchases

import ru.dreamteam.business.backends.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.{Money, Purchase, User}


trait PurchasesRepository[F[_]] {
  def findByUserId(userId: User.Id): F[List[Purchase]]
  def findByCategory(userId: User.Id, category: Purchase.Category): F[List[Purchase]]
  def findByPurchaseId(userId: User.Id, purchaseId: Purchase.Id): F[Option[Purchase]]
  def addPurchase(userId: User.Id, purchase: PurchaseRequest): F[Purchase]
}

object PurchasesRepository {
  case class PurchaseRequest(
                            // не помню, в чем мы договаривались хранить деньги в бд, пусть будет строка = число + валюта
                              money: String,
                              comment: String,
                              category: String
                            )
}