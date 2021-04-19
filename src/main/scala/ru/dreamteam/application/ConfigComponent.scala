package ru.dreamteam.application

import cats.effect.{Async, Sync}
import pureconfig.ConfigSource
import ru.dreamteam.infrastructure.MainTask
import zio.Task
import pureconfig.generic.auto._
import cats.syntax.all._

case class ConfigComponent(appConfig: AppConfig)

object ConfigComponent {
  def apply[F[_] : Sync](): F[ConfigComponent] = {
    Sync[F].fromEither(ConfigSource.defaultApplication.load[AppConfig].leftMap(
      f => new IllegalStateException(f.prettyPrint())
    ).map(ConfigComponent(_))
    )
  }
}

case class AppConfig(httpConfig: HttpConfig, dbConfig: DBConfig)
case class HttpConfig(port: Int)
case class DBConfig(url: String)