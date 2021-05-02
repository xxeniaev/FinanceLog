package ru.dreamteam.business.services.users.interpreter

import cats.Monad
import cats.effect.{BracketThrow, Sync}
import cats.ApplicativeError
import cats.syntax.all._
import ru.dreamteam.business.repository.users.UsersRepository
import ru.dreamteam.business.repository.users.UsersRepository.UserReq
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.{Token, User}

class UserServiceInterpreter[F[_] : Sync : BracketThrow : Monad](repo: UsersRepository[F], sessionService: SessionService[F]) extends UserService[F] {

  override def login(login: User.Login, password: User.Password): F[Token] =
    for {
      fromBd <- repo.findUserByLogin(login)
      user <- Sync[F].fromOption(fromBd, LoginNotExist(s"$login not exist"))
      _ <- Sync[F].raiseError(PasswordNotCorrect(s"$password not corrected")).whenA(!checkUserPassword(user, password))
      tokenString <- sessionService.generate(user.login)
    } yield tokenString

  def checkUserPassword(user: User, inputPassword: User.Password): Boolean = user.password == inputPassword

  override def registration(login: User.Login, password: User.Password): F[User] = for {
    fromBd <- repo.findUserByLogin(login)
    _ <- Sync[F].raiseError(LoginExist(s"$login exist")).whenA(fromBd.isEmpty)
    userId <- repo.addUser(UserReq(login, password))
    user = User(userId, login, password)
    token <- sessionService.generate(login)
    _ <- sessionService.addTokenUser(token, user)
  } yield user

  override def userInfo(): F[String] = ???
}

abstract class BusinessError(msg: String, th: Throwable = null) extends Exception(msg, th)

case class LoginExist(msg: String) extends BusinessError(msg)

case class PasswordNotCorrect(msg: String) extends BusinessError(msg)

case class LoginNotExist(msg: String) extends BusinessError(msg)