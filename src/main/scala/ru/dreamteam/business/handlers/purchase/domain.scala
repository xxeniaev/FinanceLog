package ru.dreamteam.business.handlers.purchase

import derevo.derive
import derevo.tethys._
import ru.dreamteam.business.Purchase
import ru.dreamteam.business.services.purchases.interpreter.PurchaseInfo
import sttp.tapir.description
import sttp.tapir.generic.auto._
import ru.dreamteam.infrastructure.newtype._

case class PurchaseInfoRequest(purchaseId: Int)

@derive(tethysReader, tethysWriter)
@description("Информация о покупке")
case class PurchaseInfoResponse(info: PurchaseInfo)

case class GetPurchasesRequest(userId: Int)

@derive(tethysReader, tethysWriter)
@description("Получить все покупки")
case class GetPurchasesResponse(
  @description("Покупки пользователя") purchases: List[Purchase]
)

case class GetPurchaseByTypeRequest(userId: Int, purchaseCategory: String)

@derive(tethysReader, tethysWriter)
@description("Получить все покупки заданного типа")
case class GetPurchaseByTypeResponse(
  @description("Покупки пользователя") purchases: List[Purchase]
)
@derive(tethysReader, tethysWriter)
case class AddPurchaseRequest(
  userId: Int,
  amount: BigDecimal,
  currency: String,
  comment: String,
  category: String
)

@derive(tethysReader, tethysWriter)
@description("Добавление покупки")
case class AddPurchaseResponse(
  @description("Идентификатор покупки") purchase: Purchase
)
