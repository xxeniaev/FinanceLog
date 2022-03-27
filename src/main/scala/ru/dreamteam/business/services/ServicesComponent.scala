package ru.dreamteam.business.services

import cats.effect.concurrent.Ref
import cats.syntax.all._
import cats.effect.{Resource, Sync}
import ru.dreamteam.business.{Token, User}
import ru.dreamteam.business.repository.RepositoriesComponent
import ru.dreamteam.business.services.purchases.PurchasesService
import ru.dreamteam.business.services.purchases.interpreter.PurchasesServiceInterpreter
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.services.session.interpreter.SessionServiceInterpreter
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.services.users.interpreter.UserServiceInterpreter
import ru.dreamteam.infrastructure.utils.RandomGeneratorImpl

case class ServicesComponent[F[_]](
  userService: UserService[F],
  purchaseService: PurchasesService[F],
  sessionService: SessionService[F]
)

object ServicesComponent {

  def build[F[_]: Sync](repoComp: RepositoriesComponent[F]): Resource[F, ServicesComponent[F]] = {

    val sessionService: F[SessionService[F]] = for {
      refTable      <- Ref[F].of(Map.empty[Token, User.Id])
      randGenerator  = new RandomGeneratorImpl[F]
      sessionService = new SessionServiceInterpreter[F](refTable, randGenerator)
    } yield sessionService

    val purchasesService = new PurchasesServiceInterpreter[F](repoComp.purchaseRepo)

    Resource.liftF(sessionService).map(sessionService =>
      ServicesComponent(
        new UserServiceInterpreter[F](sessionService, repoComp.userRepo),
        purchasesService,
        sessionService
      )
    )
  }

}
