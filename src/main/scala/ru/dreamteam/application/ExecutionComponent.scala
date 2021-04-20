package ru.dreamteam.application

import cats.Applicative
import cats.effect.{Resource, Sync}
import zio.Runtime
import zio.internal.Platform

case class ExecutionComponent(main: Runtime[Unit])

object ExecutionComponent {
  def build[F[_]: Applicative]: Resource[F, ExecutionComponent] = {
    Resource.pure(ExecutionComponent(Runtime((), Platform.default)))
  }
}
