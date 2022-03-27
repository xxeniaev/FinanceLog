package ru.dreamteam.business.services

import cats.effect.concurrent.Ref
import cats.syntax.all._
import cats.effect.{Resource, Sync}
import ru.dreamteam.business.{Token, User}
import ru.dreamteam.business.repository.RepositoriesComponent
import ru.dreamteam.business.services.session.interpreter.SessionServiceInterpreter
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.services.users.interpreter.UserServiceInterpreter
import ru.dreamteam.infrastructure.utils.RandomGeneratorImpl


case class ServicesComponent[F[_]](userService: UserService[F])

object ServicesComponent {

  def build[F[_]: Sync](repoComp: RepositoriesComponent[F]): Resource[F, ServicesComponent[F]] = {

    val userService: F[UserService[F]] = for {
      refTable <- Ref[F].of(Map.empty[Token, User.Id])
      randGenerator = new RandomGeneratorImpl[F]
      sessionService = new SessionServiceInterpreter[F](refTable, randGenerator)
      userService = new UserServiceInterpreter[F](sessionService, repoComp.userRepo)
    } yield userService

    Resource.liftF(userService).map(ServicesComponent(_))
  }
}
