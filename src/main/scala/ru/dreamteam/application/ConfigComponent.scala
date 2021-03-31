package ru.dreamteam.application

import cats.effect.{Async, Sync}
import pureconfig.ConfigSource
import ru.dreamteam.infrastructure.MainTask
import zio.Task
import pureconfig.generic.auto._
import cats.syntax.all._

case class ConfigComponent(httpConfig: HttpConfig)

object ConfigComponent {
  def apply[F[_] : Sync](): F[ConfigComponent] = {
    Sync[F].fromEither(ConfigSource.defaultApplication.load[HttpConfig].leftMap(
      f => new IllegalStateException(f.prettyPrint())
    ).map(ConfigComponent(_))
    )
  }
}

case class HttpConfig(port: Int)