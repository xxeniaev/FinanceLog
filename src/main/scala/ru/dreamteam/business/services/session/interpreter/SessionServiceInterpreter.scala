package ru.dreamteam.business.services.session.interpreter

import cats.effect.Sync
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.{Token, User}

import scala.collection.immutable.Map


// read from cats
// ref MVAR
class SessionServiceInterpreter[F[_] : Sync]() extends SessionService[F] {

  private val idTokenTable = Map.empty[Token, User]

  override def generate(login: User.Login): F[Token] = Sync[F].delay {
    Token(f"login:${login}")
  }

  override def getUser(token: Token): F[User.Id] = Sync[F].delay {
    idTokenTable.get(token)
  }

  override def deleteUserToken(token: Token): F[Unit] = Sync[F].delay {
    idTokenTable.removed(token)
  }
}
