package ru.dreamteam.business

import cats.effect.{Resource, Sync}
import ru.dreamteam.business.repositories.RepositoriesComponent
import ru.dreamteam.business.services.ServicesComponent

case class BusinessComponent[F[_]](
  repositoriesComponent: RepositoriesComponent[F],
  servicesComponent: ServicesComponent[F]
)

object BusinessComponent {

  def build[F[_]: Sync](): Resource[F, BusinessComponent[F]] = for {
    reposComp   <- RepositoriesComponent.build[F]()
    serviceComp <- ServicesComponent.build[F]()
  } yield BusinessComponent[F](reposComp, serviceComp)

}
