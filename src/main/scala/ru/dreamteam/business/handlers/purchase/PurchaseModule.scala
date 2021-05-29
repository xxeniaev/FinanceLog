package ru.dreamteam.business.handlers.purchase

import org.http4s.HttpRoutes
import ru.dreamteam.infrastructure.http.{HttpModule, Response}
import ru.dreamteam.infrastructure.{MainTask, http}
import sttp.tapir.{Endpoint, endpoint, query}
import sttp.tapir.generic.auto._
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import zio._
import cats.syntax.all._
import ru.dreamteam.business.Purchase
import ru.dreamteam.business.handlers.purchase.handlers.{AddPurchaseHandler, GetPurchaseByTypeHandler, GetPurchasesHandler, PurchaseInfoHandler}
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.services.session.SessionService
import sttp.tapir.server.ServerEndpoint
import zio.interop.catz._
import zio.interop.catz.implicits._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.codec.newtype._

class PurchaseModule(purchaseService: PurchasesService[MainTask])(
  implicit
  runtime: zio.Runtime[Unit],
  sessionService: SessionService[MainTask]
) extends HttpModule[Task] {

  val purchaseInfoEndpoint = endpoint // взять одну покупку
    .get
    .in("purchase_info")
    .in(query[Int]("purchaseId").mapTo(PurchaseInfoRequest.apply _))
    .out(jsonBody[Response[PurchaseInfoResponse]])
    .summary("Информация по пользователю")
//    .handle(PurchaseInfoHandler(purchaseService))

    .handleWithAuthorization(PurchaseInfoHandler(purchaseService))

  val getUserPurchasesEndpiont = endpoint // взять все покупки пользователя
    .get
    .in("get_purchases")
    .in(query[Int]("userId").mapTo(GetPurchasesRequest.apply _))
    .out(jsonBody[Response[GetPurchasesResponse]])
    .summary("Получение покупок пользователя")
    .handle(GetPurchasesHandler(purchaseService)(_))

//    .handleWithAuthorization(GetPurchasesHandler(purchaseService))

  val getPurchasesByTypeEndpiont = endpoint // взять покупки по типу
    .get
    .in("get_purchases_by_type")
    .in(query[Int]("userId").and(query[String]("purchaseType")).mapTo(
      GetPurchaseByTypeRequest.apply _
    ))
    .out(jsonBody[Response[GetPurchaseByTypeResponse]])
    .summary("Получение покупок по типу")
    .handle(GetPurchaseByTypeHandler(purchaseService)(_))

//    .handleWithAuthorization(GetPurchaseByTypeHandler(purchaseService))

  val addPurchaseEndpiont = endpoint // добавить покупку
    .post
    .in("add_purchase")
    .in(jsonBody[AddPurchaseRequest])
    .out(jsonBody[Response[AddPurchaseResponse]])
    .summary("Добавление покупки")
    .handle(AddPurchaseHandler(purchaseService)(_))

//    .handleWithAuthorization(AddPurchaseHandler(purchaseService))

  override def httpRoutes(
    implicit
    serverOptions: Http4sServerOptions[Task]
  ): HttpRoutes[Task] = Http4sServerInterpreter.toRoutes(
    List(
      purchaseInfoEndpoint,
      getUserPurchasesEndpiont,
      getPurchasesByTypeEndpiont,
      addPurchaseEndpiont
    )
  )

  override def endPoints: List[Endpoint[_, Unit, _, _]] = List(
    purchaseInfoEndpoint.endpoint,
    getUserPurchasesEndpiont.endpoint,
    getPurchasesByTypeEndpiont.endpoint,
    addPurchaseEndpiont.endpoint
  )

}
