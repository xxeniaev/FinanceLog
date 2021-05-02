package ru.dreamteam.business.handlers.purchase

import org.http4s.HttpRoutes
import ru.dreamteam.infrastructure.http.{HttpModule, Response}
import ru.dreamteam.infrastructure.{http, MainTask}
import sttp.tapir.{endpoint, query, Endpoint}
import sttp.tapir.generic.auto._
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import zio._
import cats.syntax.all._
import ru.dreamteam.business.Purchase
import ru.dreamteam.business.handlers.purchase.handlers.PurchaseHandler
import ru.dreamteam.business.services.purchases.PurchasesService
import sttp.tapir.server.ServerEndpoint
import zio.interop.catz._
import zio.interop.catz.implicits._
import sttp.tapir._
import sttp.tapir.generic.auto._

class PurchaseModule(purchaseService: PurchasesService[MainTask])(
  implicit
  runtime: zio.Runtime[Unit]
) extends HttpModule[Task] {
  // это все эндпоинты, которые пришли в голову, еще какие-то надо мб?

  val purchaseInfoEndpoint = endpoint
    .get
    .in("purchase_info")
    .in(query[String]("userId").mapTo(PurchaseInfoRequest.apply _))
    .out(jsonBody[Response[PurchaseInfoResponse]])
    .summary("Информация по пользователю")
    .handle(PurchaseHandler(purchaseService))

  val getUserPurchasesEndpiont = endpoint
    .get
    .in("get_purchases")
    .in(query[String]("userId").mapTo(GetPurchasesRequest.apply _))
    .out(jsonBody[Response[GetPurchasesResponse]])
    .summary("Получение покупок пользователя")
    .handle(PurchaseHandler(purchaseService))

  val getPurchasesByTypeEndpiont = endpoint
    .get
//    .in("get_purchases_by_type")
    // на уверена, что так надо, но похоже на правду
    .in(
      ("get_purchases_by_type" / path[Int]("userId") / path[String]("purchaseType")).mapTo(
        GetPurchaseByTypeRequest
      )
    )
    .out(jsonBody[Response[GetPurchaseByTypeResponse]])
    .summary("Получение покупок по типу")
    .handle(PurchaseHandler(purchaseService))

  val addPurchaseEndpiont = endpoint
    .post
    .in("add_purchase")
    .in(jsonBody[Purchase])
    .out(jsonBody[Response[PurchaseInfoResponse]])
    .summary("Добавление покупки")
    .handle(PurchaseHandler(purchaseService))

  // тут чет не особо поняла про роуты, почитаю ещё, поразбираюсь
  // нужно их несколько создать?
  override def httpRoutes(
    implicit
    serverOptions: Http4sServerOptions[Task]
  ): HttpRoutes[Task] = Http4sServerInterpreter.toRoutes(purchaseInfoEndpoint)

  override def endPoints: List[Endpoint[_, Unit, _, _]] = List(
    purchaseInfoEndpoint.endpoint,
    getUserPurchasesEndpiont.endpoint,
    getPurchasesByTypeEndpiont.endpoint,
    addPurchaseEndpiont.endpoint
  )

}
