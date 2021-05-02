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
import ru.dreamteam.business.handlers.user.{PersonalInfoRequest, PersonalInfoResponse}
import ru.dreamteam.business.handlers.user.handlers.UserHandler
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.services.users.UserService
import sttp.tapir.server.ServerEndpoint
import zio.interop.catz._
import zio.interop.catz.implicits._

class PurchaseModule(purchaseService: PurchasesService[MainTask])(
  implicit
  runtime: zio.Runtime[Unit]
) extends HttpModule[Task] {

  val purchaseInfoEndpoint = endpoint
    .get
    .in("purchase_info")
    .in(query[String]("userId").mapTo(PersonalInfoRequest.apply _))
    .out(jsonBody[Response[PurchaseInfoResponse]])
    .summary("Информация по пользователю")
    .handle(PurchaseHandler(purchaseService))

  val getPurchaseByIdEndpiont = ???

  val getUserPurchasesEndpiont = ???

  val getPurchasesByTypeEndpiont = endpoint
    .get
    .in("get_by_type")
    .in(query[List[Any]]("params"))

  ???

  val addPurchaseEndpiont = endpoint
    .post
    .in("add_purchase")
    .in(jsonBody[Purchase])
    .out(jsonBody[Response[PurchaseInfoResponse]])
    .summary("Добавление покупки")
    .handle(PurchaseHandler(purchaseService))

  override def httpRoutes(
    implicit
    serverOptions: Http4sServerOptions[Task]
  ): HttpRoutes[Task] = Http4sServerInterpreter.toRoutes(purchaseInfoEndpoint)

  override def endPoints: List[Endpoint[_, Unit, _, _]] =
    List(purchaseInfoEndpoint.endpoint, addPurchaseEndpiont.endpoint)

}
