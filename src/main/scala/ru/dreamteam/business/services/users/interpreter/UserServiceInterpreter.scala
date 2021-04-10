package ru.dreamteam.business.services.users.interpreter

import cats.effect.Sync
import cats.syntax.all._
import ru.dreamteam.business.services.SessionService
import ru.dreamteam.business.{Login, Password, Token, User}
import ru.dreamteam.business.services.users.UserService

class UserServiceInterpreter[F[_]: Sync](sessionService: SessionService[F], repo: ???) extends UserService[F] {
  override def login(login: Login, password: Password): F[Token] = {
    for {
      fromBd <- Sync[F].delay { User(???, ???) } // repo.get
      // проверить пароль
      // token <- sessionService.generate(login)
    } yield Token (???) // token

  }

  override def registration(login: Login, password: Password): F[User] = ???
}
