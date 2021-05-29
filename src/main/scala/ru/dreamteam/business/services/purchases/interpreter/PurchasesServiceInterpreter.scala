package ru.dreamteam.business.services.purchases.interpreter

import ru.dreamteam.business.Purchase.PurchaseCategory
import ru.dreamteam.business.{Money, Purchase, User}
import ru.dreamteam.business.repository.purchases.PurchasesRepository
import ru.dreamteam.business.services.purchases.PurchasesService

class PurchasesServiceInterpreter[F[_]](repo: PurchasesRepository[F]) extends PurchasesService[F] {
  override def getPurchases(userId: User.Id): F[List[Purchase]] = ???

  override def getPurchaseByType(
    userId: User.Id,
    purchaseCategory: PurchaseCategory
  ): F[List[Purchase]] = ???

  override def addPurchase(
    userId: User.Id,
    money: Money,
    comment: String,
    purchaseCategory: PurchaseCategory
  ): F[Purchase.Id] = ???

  override def purchaseInfo(userId: User.Id, purchaseId: Purchase.Id): F[String] = ???
}
