package ru.dreamteam.business.services.purchases.interpreter

import ru.dreamteam.business.{Money, Purchase, User}
import ru.dreamteam.business.backends.purchases.PurchasesRepository
import ru.dreamteam.business.services.purchases.{PurchaseType, PurchasesService}

class PurchasesServiceInterpreter[F[_]](repo: PurchasesRepository[F]) extends PurchasesService[F] {
  override def getPurchase(userId: User.Id): F[List[Purchase]] = ???

  override def getPurchaseByType(userId: User.Id, purchaseType: PurchaseType): F[List[Purchase]] = ???

  override def addPurchase(userId: User.Id, money: Money, comment: String, `type`: PurchaseType): Unit = ???
}
