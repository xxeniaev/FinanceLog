package ru.dreamteam

import ru.dreamteam.application.Application
import ru.dreamteam.infrastructure.{Context, TrackingIdGenerator}
import zio.{ExitCode, URIO, ZEnv, ZIO, ZLayer}
import zio.interop.catz._

object Starter extends zio.App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    new Application().build.use(_ => ZIO.never)
      .exitCode
      .provideLayer(ZLayer.requires[ZEnv] ++ ZLayer.fromFunction(_ => Context(TrackingIdGenerator.generate())))
}
