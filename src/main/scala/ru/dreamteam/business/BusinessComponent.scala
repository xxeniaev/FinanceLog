package ru.dreamteam.business

import cats.effect.{Resource, Sync}
import doobie.Transactor
import doobie.h2.H2Transactor
import doobie.hikari.HikariTransactor
import ru.dreamteam.business.repository.RepositoriesComponent
import ru.dreamteam.business.services.ServicesComponent

case class BusinessComponent[F[_]](
  repositoriesComponent: RepositoriesComponent[F],
  servicesComponent: ServicesComponent[F]
)

object BusinessComponent {

  def build[F[_]: Sync](transactor: Transactor[F]): Resource[F, BusinessComponent[F]] = for {
    reposComp   <- RepositoriesComponent.build[F]()
    serviceComp <- ServicesComponent.build[F](transactor)
  } yield BusinessComponent[F](reposComp, serviceComp)

}
