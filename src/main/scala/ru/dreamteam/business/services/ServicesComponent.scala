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


case class ServicesComponent[F[_]](userService: UserService[F])

object ServicesComponent {

  def build[F[_] : Sync](transactor: Transactor[F]): Resource[F, ServicesComponent[F]] = {

    val repo = new UsersRepositoryInterpreter[F](transactor)
    val sessionService = new SessionServiceInterpreter[F]
    val userService = new UserServiceInterpreter[F](sessionService = sessionService, repo = repo)
    Resource.pure(ServicesComponent(userService))
  }
}
