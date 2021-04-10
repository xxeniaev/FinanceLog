package ru.dreamteam.business.services.users.interpreter

import cats.effect.Sync
import cats.syntax.all._
import ru.dreamteam.business.services.SessionService
import ru.dreamteam.business.{Token, User}
import ru.dreamteam.business.services.users.UserService

class UserServiceInterpreter[F[_]: Sync](sessionService: SessionService[F], repo: ???) extends UserService[F] {
  override def login(login: User.Login, password: User.Password): F[Token] = {
    for {
      fromBd <- Sync[F].delay { User(???, ???) } // repo.get
      // проверить пароль
      // token <- sessionService.generate(login)
    } yield Token (???) // token

  }

  override def registration(login: User.Login, password: User.Password): F[User] = ???
}
