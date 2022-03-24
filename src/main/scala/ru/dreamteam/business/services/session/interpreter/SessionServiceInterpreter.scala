package ru.dreamteam.business.services.session.interpreter

import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.syntax.all._
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.{Token, User}
import ru.dreamteam.infrastructure.utils.RandomGenerator

import scala.collection.concurrent.Map

class SessionServiceInterpreter[F[_]: Sync](tableTokenRef: Ref[F, Map[Token, User.Id]], randomGenerator: RandomGenerator[F])
  extends SessionService[F] {

  override def generate(user: User): F[Token] = {
    for {
      token <- randomGenerator.generateRandomString(32).map(Token.apply)
      _ <- tableTokenRef.update(_.addOne(token, user.userId))
    } yield token
  }

  override def getUser(token: Token): F[Option[User.Id]] = for {
    tableToken <- tableTokenRef.get
  } yield tableToken.get(token)
}
