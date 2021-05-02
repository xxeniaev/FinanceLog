package ru.dreamteam.business

import cats.effect.{Resource, Sync}
import doobie.Transactor
import doobie.h2.H2Transactor
import doobie.hikari.HikariTransactor
import ru.dreamteam.application.DatabaseComponent
import ru.dreamteam.business.repository.RepositoriesComponent
import ru.dreamteam.business.services.ServicesComponent

case class BusinessComponent[F[_]](
  repositoriesComponent: RepositoriesComponent[F],
  servicesComponent: ServicesComponent[F]
)

object BusinessComponent {

  def build[F[_]: Sync](databaseComp: DatabaseComponent[F]): Resource[F, BusinessComponent[F]] = for {
    reposComp   <- RepositoriesComponent.build[F](databaseComp)
    serviceComp <- ServicesComponent.build[F](reposComp)
  } yield BusinessComponent[F](reposComp, serviceComp)

}
