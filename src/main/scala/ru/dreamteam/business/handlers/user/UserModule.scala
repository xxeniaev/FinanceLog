package ru.dreamteam.business.handlers.user

import org.http4s.HttpRoutes
import ru.dreamteam.infrastructure.http.{HttpModule, Response}
import ru.dreamteam.infrastructure.MainTask
import sttp.tapir.{endpoint, query, Endpoint}
import sttp.tapir.generic.auto._
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import zio._
import cats.syntax.all._
import ru.dreamteam.application.DatabaseComponent
import ru.dreamteam.business.handlers.user.handlers.{LoginHandler, PersonalInfoHandler, RegistrationHandler}
import ru.dreamteam.business.services.users.UserService
import sttp.tapir.server.ServerEndpoint
import zio.interop.catz._
import zio.interop.catz.implicits._

class UserModule(userService: UserService[MainTask])(
  implicit
  runtime: zio.Runtime[Unit]
) extends HttpModule[Task] {

  val personalInfoEndpoint = endpoint
    .get
    .in("personal_info")
    .in(query[String]("userId").mapTo(PersonalInfoRequest.apply _))
    .out(jsonBody[Response[PersonalInfoResponse]])
    .summary("Информация по пользователю")
    .handle(PersonalInfoHandler(userService))

  val registrationEndpoint = endpoint
    .post
    .in("register")
    .in(jsonBody[Credentials])
    .out(jsonBody[Response[RegistrationResponse]])
    .summary("Регистрация пользователя")
    .description("Пользователь регистрируется, создавая логин и пароль")
    .handle(RegistrationHandler(userService))

  val loginEndpoint = endpoint
    .post
    .in("login")
    .in(jsonBody[Credentials])
    .out(jsonBody[Response[LoginResponse]])
    .summary("Вход пользователя")
    .description("Пользователь логинится, указывая свои логин и пароль")
    .handle(LoginHandler(userService))

  override def httpRoutes(
    implicit
    serverOptions: Http4sServerOptions[Task]
  ): HttpRoutes[Task] = Http4sServerInterpreter.toRoutes(List(
    personalInfoEndpoint,
    registrationEndpoint,
    loginEndpoint
  ))

  override def endPoints: List[Endpoint[_, Unit, _, _]] =
    List(personalInfoEndpoint.endpoint, registrationEndpoint.endpoint, loginEndpoint.endpoint)

}
