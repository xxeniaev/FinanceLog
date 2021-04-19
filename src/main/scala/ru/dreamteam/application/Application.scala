package ru.dreamteam.application

import cats.effect.{ContextShift, IO, Resource, Timer}
import org.http4s.HttpRoutes
import org.http4s.server.{Router, Server}
import org.http4s.server.blaze.BlazeServerBuilder
import ru.dreamteam.infrastructure.MainTask
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.{Endpoint, endpoint, query, stringBody}
import org.http4s.syntax.kleisli._
import sttp.tapir._

import scala.concurrent.ExecutionContext
import cats.syntax.all._
import ru.dreamteam.application.ServerComponent.Modules
import ru.dreamteam.business.BusinessComponent
import zio.ZIO
import zio.interop.catz._

class Application {
  import Resource.{liftF => rLiftF}


//  val helloWorld: Endpoint[String, Unit, String, Any] =
//    endpoint.get.in("hello").in(query[String]("name")).out(stringBody)
//
//  // mandatory implicits
//  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
//  implicit val contextShift: ContextShift[MainTask] = IO.contextShift(ec)
//  implicit val timer: Timer[MainTask] = ZIO.timer(ec)
//
//  // converting an endpoint to a route (providing server-side logic); extension method comes from imported packages
//  val helloWorldRoutes: HttpRoutes[MainTask] =
//    Http4sServerInterpreter.toRoutes(helloWorld)(name =>
//      IO(s"Hello, $name!".asRight[Unit]))
//
//  // starting the server
//  val server: Resource[MainTask, Server[MainTask]] =
//    BlazeServerBuilder[MainTask](ec)
//      .bindHttp(8080, "localhost")
//      .withHttpApp(Router("/" -> helloWorldRoutes).orNotFound)
//      .resource


  def build: Resource[MainTask, Unit] = for {
    configComp <- rLiftF(ConfigComponent[MainTask]())
     executorsComp <- ExecutionComponent.build[MainTask]
     databaseComp <- DatabaseComponent.build[MainTask](configComp.appConfig.dbConfig)
    // httpClientComp <- HttpClientComponent
     businessComp <- BusinessComponent.build[MainTask](???)
     server <- ServerComponent.build(
       Modules(
          system = List(businessComp.systemModule),
          public = List(businessComp.userModule)
       )
     )(configComp.appConfig.httpConfig, executorsComp.main)

  } yield ()
}
