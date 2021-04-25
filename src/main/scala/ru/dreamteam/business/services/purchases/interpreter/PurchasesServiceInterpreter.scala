package ru.dreamteam.business.services.purchases.interpreter

import ru.dreamteam.business.Purchase.{Comment, PurchaseType}
import ru.dreamteam.business.{Money, Purchase, User}
import ru.dreamteam.business.repository.purchases.PurchasesRepository
import ru.dreamteam.business.repository.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.services.purchases.PurchasesService

class PurchasesServiceInterpreter[F[_]](repo: PurchasesRepository[F]) extends PurchasesService[F] {
  implicit def stringToPurchaseComment(str: String): Comment = Comment(str)

  override def getPurchase(userId: User.Id): F[List[Purchase]] = repo.findByUserId(userId)

  override def getPurchaseByType(userId: User.Id, purchaseType: PurchaseType): F[List[Purchase]] = repo.findByCategory(userId, purchaseType)

  override def addPurchase(userId: User.Id, money: Money, comment: String, `type`: PurchaseType): Unit = repo.addPurchase(userId, PurchaseRequest(money, comment, `type`))
}
