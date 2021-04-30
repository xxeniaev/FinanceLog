package ru.dreamteam.business.handlers.purchase.handlers

import ru.dreamteam.business.handlers.purchase.{PurchaseInfoRequest, PurchaseInfoResponse}
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.{Purchase, User}
import ru.dreamteam.infrastructure.{MainEnv, MainTask}
import zio.ZIO

object PurchaseHandler {

  def apply[R](
    purchasesService: PurchasesService[MainTask]
  )(req: PurchaseInfoRequest): ZIO[MainEnv, Throwable, PurchaseInfoResponse] =
    purchasesService.purchaseInfo(User.Id(req.userId), Purchase.Id(req.purchaseId)).map(_ =>
      PurchaseInfoResponse(???)
    )

}
