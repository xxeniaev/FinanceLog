package ru.dreamteam.business.services

import cats.effect.{Resource, Sync}
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.services.users.interpreter.UserServiceInterpreter


case class ServicesComponent[F[_]](userService: UserService[F])

object ServicesComponent {

  def build[F[_]: Sync](): Resource[F, ServicesComponent[F]] = {

    val userService = new UserServiceInterpreter[F](???, ???)

    Resource.pure(ServicesComponent(userService))
  }
}
