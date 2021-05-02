package ru.dreamteam.business.repository

import cats.effect.{Resource, Sync}
import ru.dreamteam.business.repository.users.interpreter.UsersRepositoryInterpreter
import cats.syntax.all._
import ru.dreamteam.application.DatabaseComponent
import ru.dreamteam.business.repository.purchases.PurchasesRepository
import ru.dreamteam.business.repository.purchases.interpreter.PurchaseRepositoryInterpreter
import ru.dreamteam.business.repository.users.UsersRepository

case class RepositoriesComponent[F[_]](userRepo: UsersRepository[F], purchaseRepo: PurchasesRepository[F])

object RepositoriesComponent {
  def build[F[_]: Sync](databaseComponent: DatabaseComponent[F]): Resource[F, RepositoriesComponent[F]] = {
    val userRepo = new UsersRepositoryInterpreter[F](databaseComponent.transactor)
    val purchaseRepo = new PurchaseRepositoryInterpreter[F](databaseComponent.transactor)
    Resource.pure(RepositoriesComponent[F](userRepo, purchaseRepo))
  }
}
