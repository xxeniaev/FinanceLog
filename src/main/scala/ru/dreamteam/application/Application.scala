package ru.dreamteam.application

import cats.effect.{ContextShift, IO, Resource, Timer}
import org.http4s.HttpRoutes
import org.http4s.server.{Router, Server}
import org.http4s.server.blaze.BlazeServerBuilder
import ru.dreamteam.infrastructure.{MainEnv, MainTask}
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.{Endpoint, endpoint, query, stringBody}
import org.http4s.syntax.kleisli._
import sttp.tapir._

import scala.concurrent.ExecutionContext
import cats.syntax.all._
import cats._
import ru.dreamteam.application.ServerComponent.Modules
import ru.dreamteam.business.BusinessComponent
import ru.dreamteam.business.handlers.system.SystemModule
import ru.dreamteam.business.handlers.user.UserModule
import zio.{Task, ZIO, ZLayer}
import zio.interop.catz._
import zio.interop.catz.implicits._
import cats.implicits._
import ru.dreamteam.business.handlers.purchase.PurchaseModule

class Application {
  import Resource.{liftF => rLiftF}

  implicit val fk : Task ~> MainTask = λ[Task ~> MainTask](task => task)

  def build: Resource[MainTask, Unit] = for {
    configComp    <- rLiftF(ConfigComponent[MainTask]())
    executorsComp <- ExecutionComponent.build[MainTask]
    implicit0(runtime: zio.Runtime[Unit]) = executorsComp.main
    databaseComp  <- DatabaseComponent.build[MainTask](configComp.appConfig.dbConfig)
    // httpClientComp <- HttpClientComponent
    businessComp  <- BusinessComponent.build[MainTask](databaseComp.transactor)
    server        <- ServerComponent.build(
                       Modules(
                         system = List(new SystemModule()(executorsComp.main)),
                         public = List(new UserModule(businessComp.servicesComponent.userService)(executorsComp.main),
                           new PurchaseModule(businessComp.servicesComponent.purchaseService)(executorsComp.main, businessComp.servicesComponent.sessionService))
                       )
                     )(configComp.appConfig.httpConfig, executorsComp.main)
    _ = println("DOME")
  } yield ()

}
