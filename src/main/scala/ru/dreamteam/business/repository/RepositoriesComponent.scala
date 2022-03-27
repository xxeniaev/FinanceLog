package ru.dreamteam.business.repository

import cats.effect.{Resource, Sync}
import doobie.util.transactor.Transactor
import ru.dreamteam.business.repository.purchases.PurchasesRepository
import ru.dreamteam.business.repository.purchases.interpreter.PurchaseRepositoryInterpreter
import ru.dreamteam.business.repository.users.UsersRepository
import ru.dreamteam.business.repository.users.interpreter.UsersRepositoryInterpreter

case class RepositoriesComponent[F[_]](userRepo: UsersRepository[F], purchaseRepo: PurchasesRepository[F])

object RepositoriesComponent {
  def build[F[_]: Sync](transactor: Transactor[F]): Resource[F, RepositoriesComponent[F]] = {

    val userRepo = new UsersRepositoryInterpreter[F](transactor)
    val purchaseRepo = new PurchaseRepositoryInterpreter[F](transactor)

    Resource.pure(RepositoriesComponent[F](userRepo, purchaseRepo))
  }
}
