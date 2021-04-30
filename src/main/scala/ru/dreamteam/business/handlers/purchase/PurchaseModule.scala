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
import ru.dreamteam.business.handlers.purchase.handlers.PurchaseHandler
import ru.dreamteam.business.handlers.user.{PersonalInfoRequest, PersonalInfoResponse}
import ru.dreamteam.business.handlers.user.handlers.UserHandler
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.services.users.UserService
import sttp.tapir.server.ServerEndpoint
import zio.interop.catz._
import zio.interop.catz.implicits._

class UserModule(purchaseService: PurchasesService[MainTask])(
  implicit
  runtime: zio.Runtime[Unit]
) extends HttpModule[Task] {

  val purchaseInfoEndpoint = ???

  val getPurchasesEndpiont = ???

  val getPurchasesByTypeEndpiont = ???

  val addPurchaseEndpiont = ???

  override def httpRoutes(
    implicit
    serverOptions: Http4sServerOptions[Task]
  ): HttpRoutes[Task] = Http4sServerInterpreter.toRoutes(???)

  override def endPoints: List[Endpoint[_, Unit, _, _]] = List(???)

}
