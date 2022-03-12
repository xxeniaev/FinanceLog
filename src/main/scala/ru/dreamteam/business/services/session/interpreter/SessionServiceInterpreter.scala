package ru.dreamteam.business.services.session.interpreter

import cats.Monad
import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.syntax.all._
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.{Token, User}

import scala.collection.concurrent.Map



class SessionServiceInterpreter[F[_]: Sync: Monad](tableToken: Ref[F, Map[Token, User.Id]]) extends SessionService[F] {

  override def generate(user: User): F[Token] = for {
    token <- Sync[F].delay(Token(s"userId: ${user.userId}, userLogin: ${user.login}"))
    _ <- tableToken.update(x => x.addOne(token, user.userId))
  } yield token

  override def getUser(token: Token): F[Option[User.Id]] = for {
    map <- tableToken.get
    userId = map.get(token)
  } yield userId
}
