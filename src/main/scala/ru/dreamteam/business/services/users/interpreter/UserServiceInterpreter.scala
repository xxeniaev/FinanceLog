package ru.dreamteam.business.services.users.interpreter

import cats.Monad
import cats.syntax.all._
import cats.effect.concurrent.Ref
import cats.effect.{BracketThrow, Sync}
import ru.dreamteam.business.repository.users.UsersRepository
import ru.dreamteam.business.repository.users.UsersRepository.UserReq
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.{Token, User}


class UserServiceInterpreter[F[_] : Sync : BracketThrow : Monad](sessionService: SessionService[F],
                                                                 repo: UsersRepository[F]
                                                                ) extends UserService[F] {

  override def login(login: User.Login, password: User.Password): F[Token] = for {
    userOption <- repo.findUserByLogin(login)
    user <- Sync[F].fromOption(userOption, LoginNotExist(""))
    _ <- Sync[F].raiseError(IncorrectPassword("incorrect password")).whenA(user.password != password)
    token <- sessionService.generate(user)
  } yield token

  override def registration(login: User.Login, password: User.Password): F[User] = for {
    userOption <- repo.findUserByLogin(login)
    _ <- Sync[F].raiseError(LoginExist("login already exists")).whenA(userOption.isDefined)
    userId <- repo.addUser(UserReq(login, password))
  } yield User(userId, login, password)

  override def userInfo(userId: User.Id): F[String] = Sync[F].delay(s"userId: $userId")
}

abstract class BusinessError(msg: String, th: Throwable = null) extends Exception(msg, th)

case class LoginNotExist(msg: String) extends BusinessError(msg)

case class LoginExist(msg: String) extends BusinessError(msg)

case class IncorrectPassword(msg: String) extends BusinessError(msg)