package ru.dreamteam.business.handlers.user

import org.http4s.HttpRoutes
import ru.dreamteam.infrastructure.http.{HttpModule, Response}
import ru.dreamteam.infrastructure.{MainTask, http}
import sttp.tapir.{Endpoint, endpoint, query}
import sttp.tapir.generic.auto._
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import zio._
import cats.syntax.all._
import ru.dreamteam.business.handlers.user.handlers.UserHandler
import ru.dreamteam.business.services.users.UserService
import zio.interop.catz._
import zio.interop.catz.implicits._

class UserModule(userService: UserService[MainTask])(implicit runtime: zio.Runtime[Unit]) extends HttpModule[Task] {

  val personalInfoEndpoint =
    endpoint
      .get
      .in("personal_info")
      .in(query[String]("userId").mapTo(PersonalInfoRequest.apply _))
      .out(jsonBody[Response[PersonalInfoResponse]])
      .handle(UserHandler(userService))

  override def httpRoutes(implicit serverOptions: Http4sServerOptions[Task]): HttpRoutes[Task] =
    Http4sServerInterpreter.toRoutes(personalInfoEndpoint)
}
