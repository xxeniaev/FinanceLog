package ru.dreamteam.business.services.users.interpreter

import cats.Monad
import cats.effect.{BracketThrow, Sync}
import cats.syntax.all._
import doobie.implicits.toSqlInterpolator
import ru.dreamteam.business.User.Password
import ru.dreamteam.business.repository.purchases.interpreter.PurchaseRepositoryInterpreter.PurchaseRaw
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.{Token, User}
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.repository.users.UsersRepository
import ru.dreamteam.business.repository.users.interpreter.UsersRepositoryInterpreter.UserRaw

class UserServiceInterpreter[F[_]: Sync: BracketThrow](
  sessionService: SessionService[F],
  repo: UsersRepository[F]
) extends UserService[F] {

  override def login(login: User.Login, password: User.Password): F[Token] = ???

  override def registration(login: User.Login, password: User.Password): F[User] = ???

  override def userInfo(userId: User.Id): F[String] = Sync[F].raiseError(LoginExist("not bad"))
}

abstract class BusinessError(msg: String, th: Throwable = null) extends Exception(msg, th)
case class LoginExist(msg: String) extends BusinessError(msg)