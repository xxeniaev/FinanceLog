package ru.dreamteam.business.repository

import cats.effect.{Resource, Sync}
import ru.dreamteam.application.DatabaseComponent
import ru.dreamteam.business.repository.users.interpreter.UsersRepositoryInterpreter
import ru.dreamteam.business.repository.purchases.PurchasesRepository
import ru.dreamteam.business.repository.purchases.interpreter.PurchaseRepositoryInterpreter
import ru.dreamteam.business.repository.users.UsersRepository

case class RepositoriesComponent[F[_]](userRepo: UsersRepository[F], purchaseRepo: PurchasesRepository[F])

object RepositoriesComponent {
  def build[F[_]: Sync](databaseComp: DatabaseComponent[F]): Resource[F, RepositoriesComponent[F]] = {
    val usersRepository = new UsersRepositoryInterpreter[F](databaseComp.transactor)
    val purchasesRepository = new PurchaseRepositoryInterpreter[F](databaseComp.transactor)
    Resource.pure(RepositoriesComponent[F](usersRepository, purchasesRepository))
  }
}
