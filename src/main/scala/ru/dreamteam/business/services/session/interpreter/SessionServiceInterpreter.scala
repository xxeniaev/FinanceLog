package ru.dreamteam.business.services.session.interpreter

import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.syntax.all._
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.{Token, User}

import scala.collection.concurrent.Map


class SessionServiceInterpreter[F[_] : Sync](tableToken: Ref[F, Map[Token, User.Id]]) extends SessionService[F] {

  override def generate(user: User): F[Token] = for {
    token <- generateToken(user.userId, user.login)
    _ <- tableToken.update(_.addOne(token, user.userId))
  } yield token

  private def generateToken(userId: User.Id, userLogin: User.Login): F[Token] =
    Sync[F].delay(Token(s"userId: $userId, userLogin: $userLogin"))

  override def getUser(token: Token): F[Option[User.Id]] = for {
    userId <- tableToken.get.map(_.get(token))
  } yield userId
}
