package ru.dreamteam.business.services.purchases.interpreter

import ru.dreamteam.business.Purchase.{Comment, PurchaseCategory}
import ru.dreamteam.business.{Money, Purchase, User}
import ru.dreamteam.business.repository.purchases.PurchasesRepository
import ru.dreamteam.business.repository.purchases.PurchasesRepository.PurchaseRequest
import ru.dreamteam.business.services.purchases.PurchasesService

class PurchasesServiceInterpreter[F[_]](repo: PurchasesRepository[F]) extends PurchasesService[F] {
  override def getPurchase(userId: User.Id): F[List[Purchase]] = repo.findByUserId(userId)

  override def getPurchaseByType(
    userId: User.Id,
    purchaseCategory: PurchaseCategory
  ): F[List[Purchase]] = repo.findByCategory(userId, purchaseCategory)

  override def addPurchase(
    userId: User.Id,
    money: Money,
    comment: String,
    purchaseCategory: PurchaseCategory
  ): Unit = repo.addPurchase(userId, PurchaseRequest(money, Comment(comment), purchaseCategory))

}
