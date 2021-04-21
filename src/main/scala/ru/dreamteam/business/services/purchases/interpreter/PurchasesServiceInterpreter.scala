package ru.dreamteam.business.services.purchases.interpreter

import ru.dreamteam.business.Purchase.{Category, Comment, PurchaseType}
import ru.dreamteam.business.backends.purchases.PurchasesRepository
import ru.dreamteam.business.backends.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.{Money, Purchase, User}

class PurchasesServiceInterpreter[F[_]](repo: PurchasesRepository[F]) extends PurchasesService[F] {
  implicit def purchaseTypeToCategory(purchaseType: PurchaseType): Category = Category(purchaseType)

  //там написано, что лишняя вложенность
  // пока такая затычка, потом когда уберем, можно просто передать метод repo под PurchaseType

  implicit def stringToPurchaseComment(str: String): Comment = Comment(str)

  override def getPurchase(userId: User.Id): F[List[Purchase]] = repo.getAllPurchases(userId)

  override def getPurchaseByType(userId: User.Id, purchaseType: PurchaseType): F[List[Purchase]] = repo.getPurchasesByCategory(userId, purchaseType)

  override def addPurchase(userId: User.Id, money: Money, comment: String, `type`: PurchaseType): Unit = repo.addPurchase(userId, PurchaseRequest(money, comment, `type`))
}
