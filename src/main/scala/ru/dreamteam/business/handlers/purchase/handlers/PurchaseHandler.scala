package ru.dreamteam.business.handlers.purchase.handlers

import ru.dreamteam.business.handlers.purchase.{GetPurchasesRequest, GetPurchasesResponse, PurchaseInfoRequest, PurchaseInfoResponse}
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.{Purchase, User}
import ru.dreamteam.infrastructure.{MainEnv, MainTask}
import zio.ZIO

object PurchaseHandler {

  def apply[R](
    purchasesService: PurchasesService[MainTask]
  )(req: User.Id): ZIO[MainEnv, Throwable, PurchaseInfoResponse] =
    purchasesService.getPurchases(req).map(info =>
      ???
    )

}


object GetPurchasesHandler {

  def apply[R](
                purchasesService: PurchasesService[MainTask]
              )(req: GetPurchasesRequest): ZIO[MainEnv, Throwable, GetPurchasesResponse] =
    for {
      info <- purchasesService.getPurchases(User.Id(req.userId))
    } yield GetPurchasesResponse(info)

}
