package ru.dreamteam.business.handlers.purchase.handlers

import ru.dreamteam.business.Purchase.PurchaseCategory
import ru.dreamteam.business.handlers.purchase.{AddPurchaseRequest, AddPurchaseResponse, GetPurchaseByTypeRequest, GetPurchaseByTypeResponse, GetPurchasesRequest, GetPurchasesResponse, PurchaseInfoRequest, PurchaseInfoResponse}
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.{Currency, Money, Purchase, User}
import ru.dreamteam.infrastructure.{MainEnv, MainTask}
import zio.ZIO

object PurchaseInfoHandler {

  def apply[R](
    purchasesService: PurchasesService[MainTask]
  )(req: PurchaseInfoRequest): ZIO[MainEnv, Throwable, PurchaseInfoResponse] =
    purchasesService.purchaseInfo(User.Id(req.userId), Purchase.Id(req.purchaseId))
      .map(info => PurchaseInfoResponse(info))

}

object GetPurchasesHandler {

  def apply[R](
    purchasesService: PurchasesService[MainTask]
  )(req: GetPurchasesRequest): ZIO[MainEnv, Throwable, GetPurchasesResponse] =
    purchasesService.getPurchases(User.Id(req.userId)).map(purchases =>
      GetPurchasesResponse(purchases)
    )

  // по этому как-то удобнее ориентироваться, оставлю себе для примера
//  def apply[R](
//    purchasesService: PurchasesService[MainTask]
//  )(req: GetPurchasesRequest): ZIO[MainEnv, Throwable, GetPurchasesResponse] = for {
//    info <- purchasesService.getPurchases(User.Id(req.userId))
//  } yield GetPurchasesResponse(info)

}

object GetPurchaseByTypeHandler {

  def apply[R](
    purchasesService: PurchasesService[MainTask]
  )(req: GetPurchaseByTypeRequest): ZIO[MainEnv, Throwable, GetPurchaseByTypeResponse] =
    purchasesService.getPurchaseByType(
      User.Id(req.userId),
      PurchaseCategory.parse(req.purchaseCategory)
    ).map(purchases => GetPurchaseByTypeResponse(purchases))

}

object AddPurchaseHandler {

  def apply[R](
    purchasesService: PurchasesService[MainTask]
  )(req: AddPurchaseRequest): ZIO[MainEnv, Throwable, AddPurchaseResponse] =
    purchasesService.addPurchase(
      User.Id(req.userId),
      Money(req.amount, Currency.parse(req.currency)),
      req.comment,
      PurchaseCategory.parse(req.category)
    ).map(info => AddPurchaseResponse(info))

}
