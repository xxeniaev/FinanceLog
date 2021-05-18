package ru.dreamteam.business.services.session.interpreter

import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.implicits._
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.{Token, User}

import scala.collection.immutable.Map

class SessionServiceInterpreter[F[_] : Sync](tokenTableRef: Ref[F, Map[Token, User.Id]]) extends SessionService[F] {

  override def generate(userId: User.Id): F[Token] =
    for {
      session <- generateSession(userId)
      _ <- tokenTableRef.update(old => old + (session -> userId))
    } yield session

  override def getUserId(token: Token): F[Option[User.Id]] =
    tokenTableRef.get.map(_.get(token))

  override def removeToken(token: Token): F[Unit] =
    tokenTableRef.update(_ - token)


  private def generateSession(userId: User.Id): F[Token] = Sync[F].delay {
    Token(s"$userId")
  }
}
