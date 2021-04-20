package ru.dreamteam.application

import cats.~>
import cats.effect.{ConcurrentEffect, ContextShift, Resource, Timer}
import org.http4s.HttpRoutes
import org.http4s.server.{Router, Server}
import org.http4s.server.blaze.BlazeServerBuilder
import ru.dreamteam.infrastructure.MainTask
import ru.dreamteam.infrastructure.http.HttpModule
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import zio._
import zio.interop.catz._
import org.http4s._
import org.http4s.syntax.kleisli._
import cats.syntax.all._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import sttp.tapir.{endpoint, path}
import zio.interop.catz.implicits._
import sttp.tapir.docs.openapi._
import sttp.tapir.json.circe._
import sttp.tapir.openapi.circe.yaml._

case class ServerComponent()

object ServerComponent {

  case class Modules[F[_]](system: List[HttpModule[F]], public: List[HttpModule[F]])

  def build[F[_]: ConcurrentEffect: Timer: ContextShift](modules: Modules[F])(
    httpConfig: HttpConfig,
    runtime: zio.Runtime[Unit]
  )(
    implicit
    mapK: F ~> MainTask
  ): Resource[MainTask, ServerComponent] = {

    implicit val serverOptions: Http4sServerOptions[F] = Http4sServerOptions.default[F]

    def bind(httpModules: List[HttpModule[F]], port: Int): Resource[MainTask, Server[F]] = {

      val docs: OpenAPI = OpenAPIDocsInterpreter.toOpenAPI(httpModules.flatMap(_.endPoints), "My Bookshop", "1.0")
      val swagger = new SwaggerHttp4s(docs.toYaml)

      BlazeServerBuilder[F](runtime.platform.executor.asEC)
        .bindHttp(port, "localhost")
        .withHttpApp(Router(
          "/" -> (httpModules.foldLeft(HttpRoutes.empty[F])((acc, r) =>
            acc <+> r.httpRoutes
          ) <+> swagger.routes)
        ).orNotFound)
        .resource
        .mapK[F, MainTask](mapK)
    }
    for {
      system <- bind(modules.system, httpConfig.systemPort)
      public <- bind(modules.public, httpConfig.publicPort)
    } yield ServerComponent()
  }

}
