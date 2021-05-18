package ru.dreamteam.business.services

import cats.effect.{IO, Resource, Sync}
import doobie.Transactor
import ru.dreamteam.business.User
import ru.dreamteam.business.repository.users.interpreter.UsersRepositoryInterpreter
import ru.dreamteam.business.services.session.interpreter.SessionServiceInterpreter
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.services.users.interpreter.UserServiceInterpreter
import cats.syntax.all._
import doobie.hikari.HikariTransactor
import ru.dreamteam.business.repository.RepositoriesComponent
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.services.purchases.interpreter.PurchasesServiceInterpreter
import ru.dreamteam.business.services.session.SessionService


case class ServicesComponent[F[_]](userService: UserService[F], sessionService: SessionService[F], purchasesService: PurchasesService[F])

object ServicesComponent {

  def build[F[_] : Sync](repoComp: RepositoriesComponent[F]): Resource[F, ServicesComponent[F]] = {
    // проинизиализировать Ref использовать for-comp, вероятно надо будет Resource.lift(...)
    val sessionService = new SessionServiceInterpreter[F](???)
    val purchasesService = new PurchasesServiceInterpreter[F](repoComp.purchaseRepo)
    val userService = new UserServiceInterpreter[F](repoComp.userRepo, sessionService)
    Resource.pure(ServicesComponent(userService, sessionService, purchasesService))
  }
}
