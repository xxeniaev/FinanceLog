package ru.dreamteam.business.services.users.interpreter

import cats.Monad
import cats.effect.{BracketThrow, Sync}
import cats.syntax.all._
import ru.dreamteam.business.repository.users.UsersRepository
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.{Token, User}

class UserServiceInterpreter[F[_] : Sync : BracketThrow : Monad](repo: UsersRepository[F], sessionService: SessionService[F]) extends UserService[F] {

  override def login(login: User.Login, password: User.Password): F[Token] =
    for {
      fromBd <- repo.findUserByLogin(login).flatMap {
        case Some(value) => Sync[F].delay(value)
        case None => Sync[F].raiseError(LoginNotExist(s"$login don't exists"))
      }
      loggedUser <- checkUserPassword(fromBd, password) match {
        case true => Sync[F].delay(fromBd)
        case false => Sync[F].raiseError(PasswordNotCorrect(s"password don't correct"))
      }
      tokenString <- sessionService.generate(loggedUser.login)
    } yield tokenString

  def checkUserPassword(user: User, inputPassword: User.Password): Boolean = user.password == inputPassword

  override def registration(login: User.Login, password: User.Password): F[User] = ???

  //override def userInfo(): F[String] = Sync[F].raiseError(LoginExist("not bad"))
}

abstract class BusinessError(msg: String, th: Throwable = null) extends Exception(msg, th)

case class LoginExist(msg: String) extends BusinessError(msg)

case class PasswordNotCorrect(msg: String) extends BusinessError(msg)

case class LoginNotExist(msg: String) extends BusinessError(msg)