package ru.dreamteam.business.services.users.interpreter

import cats.Monad
import cats.effect.{BracketThrow, Sync}
import cats.syntax.all._
import doobie.implicits.toSqlInterpolator
import ru.dreamteam.business.User.Password
import ru.dreamteam.business.backends.purchases.interpreter.PurchaseRepositoryInterpreter.PurchaseRaw
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.{Token, User}
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.backends.users.UsersRepository
import ru.dreamteam.business.backends.users.interpreter.UsersRepositoryInterpreter.UserRaw

class UserServiceInterpreter[F[_]: Sync: BracketThrow: Monad](sessionService: SessionService[F], repo: UsersRepository[F]) extends UserService[F] {
  override def login(login: User.Login, password: User.Password): F[Token] = {
//    for {
//      fromBd <- Sync[F].delay { User(???, ???) } // repo.get
//      // проверить пароль
//      isPasswordCorrect <- repo.checkPassword(login, password)
//      token <- sessionService.generate(login)
//    } yield Token (???) // token
  ???
  }

  override def registration(login: User.Login, password: User.Password): F[User] = ???
}
