package ru.dreamteam.application

import cats._
import cats.effect.Resource
import ru.dreamteam.application.ServerComponent.Modules
import ru.dreamteam.business.{BusinessComponent, User}
import ru.dreamteam.business.handlers.system.SystemModule
import ru.dreamteam.business.handlers.user.UserModule
import ru.dreamteam.infrastructure.MainTask
import zio.Task
import zio.interop.catz._
import zio.interop.catz.implicits._

class Application {

  import Resource.{liftF => rLiftF}

  implicit val fk: Task ~> MainTask = λ[Task ~> MainTask](task => task)

  def build: Resource[MainTask, Unit] = for {
    configComp <- rLiftF(ConfigComponent[MainTask]())
    executorsComp <- ExecutionComponent.build[MainTask]
    implicit0(runtime: zio.Runtime[Unit]) = executorsComp.main
    databaseComp <- DatabaseComponent.build[MainTask](configComp.appConfig.dbConfig)
    // httpClientComp <- HttpClientComponent
    businessComp <- BusinessComponent.build[MainTask](databaseComp)
    server <- ServerComponent.build(
      Modules(
        system = List(new SystemModule()(executorsComp.main)),
        public = List(new UserModule(businessComp.servicesComponent.userService)(executorsComp.main))
      )
    )(configComp.appConfig.httpConfig, executorsComp.main)
    _ = println("DOME")
  } yield ()

}
