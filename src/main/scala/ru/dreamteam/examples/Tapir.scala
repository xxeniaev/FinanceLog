package ru.dreamteam.examples

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.{Router, Server}
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import sttp.tapir._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.syntax.all._
import sttp.tapir.server.ServerEndpoint

import scala.concurrent.ExecutionContext

object Tapir extends App {
  // the endpoint: single fixed path input ("hello"), single query parameter
  // corresponds to: GET /hello?name=...
  val helloWorld: Endpoint[String, Unit, String, Any] =
    endpoint.get.in("hello").in(query[String]("name")).out(stringBody)

  // mandatory implicits
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)
  implicit val timer: Timer[IO] = IO.timer(ec)

  val serverEndpoint: List[ServerEndpoint[String, Unit, String, Any, IO]] =
    List(helloWorld.serverLogic(name => IO(s"Hello, $name!".asRight[Unit])))
  // converting an endpoint to a route (providing server-side logic); extension method comes from imported packages
  val helloWorldRoutes: HttpRoutes[IO] =
    Http4sServerInterpreter.toRoutes(serverEndpoint)

  // starting the server
  val server: Resource[IO, Server[IO]] =
    BlazeServerBuilder[IO](ec)
      .bindHttp(8080, "localhost")
      .withHttpApp(Router("/" -> helloWorldRoutes).orNotFound)
      .resource

  server.use(_ => IO.never).unsafeRunSync()
}
