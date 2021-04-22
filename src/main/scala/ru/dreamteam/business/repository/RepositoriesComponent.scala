package ru.dreamteam.business.repository

import cats.effect.{Resource, Sync}

case class RepositoriesComponent[F[_]]()

object RepositoriesComponent {
  def build[F[_]: Sync](): Resource[F, RepositoriesComponent[F]] = {
    Resource.pure(RepositoriesComponent[F]())
  }
}
