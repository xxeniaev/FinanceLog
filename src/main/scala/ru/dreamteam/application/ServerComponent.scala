package ru.dreamteam.application

import cats.effect.Resource
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import ru.dreamteam.infrastructure.MainTask
import ru.dreamteam.infrastructure.http.HttpModule
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import zio._
import zio.interop.catz._
import org.http4s._
import org.http4s.syntax.kleisli._
import cats.syntax.all._
import zio.interop.catz.implicits._

case class ServerComponent()

object ServerComponent {

  case class Modules[F[_]](system: List[HttpModule[F]], public: List[HttpModule[F]])

  def build[F[_]](modules: Modules[F])(httpConfig: HttpConfig, runtime: zio.Runtime[Unit]): Resource[MainTask, ServerComponent] = {

    implicit val run = runtime
    implicit val serverOptions: Http4sServerOptions[Task] = Http4sServerOptions.default[Task]

    def bind(endPoint: List[HttpModule[Task]], port: Int): Resource[MainTask, Int] = {
      BlazeServerBuilder[Task](runtime.platform.executor.asEC)
        .bindHttp(port, "localhost")
        .withHttpApp(Router("/" -> endPoint.foldLeft(HttpRoutes.empty[Task])((acc, r) => acc <+> r.httpRoutes)).orNotFound)
        .resource
      ???
    }
    ???
//    val routes: HttpRoutes[Task] =
//      Http4sServerInterpreter
//        .toRoutes()((logic _).tupled)
  }
}
