package ru.dreamteam.business.services.session.interpreter

import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.implicits._
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.{Token, User}

import scala.collection.immutable.Map


// read from cats
// ref MVAR
class SessionServiceInterpreter[F[_] : Sync]() extends SessionService[F] {

  private val tokenTableRef: Ref[F, Map[Token, User]] = Ref.unsafe(Map.empty)

  override def generate(login: User.Login): F[Token] = Sync[F].delay {
    Token(s"$login")
  }

  override def getUserId(token: Token): F[Option[User.Id]] = {
    val user = tokenTableRef.get.map(_.get(token)).map {
      case Some(value) => Some(value.userId)
      case None => None
    }
    user
  }

  override def addTokenUser(token: Token, user: User): F[Unit] = Sync[F].delay {
    tokenTableRef.update(_ + (token -> user))
    printTable()
  }

  override def removeToken(token: Token): F[Unit] = Sync[F].delay {
    tokenTableRef.update(_ - token)
    printTable()
  }

  private def printTable():Unit = {
    print("______")
    for {
      keys <- tokenTableRef.get.map(_.keys)
      s = keys.map(x => print(x, tokenTableRef.get.map(_.get(x))))
    } yield Unit
    print("______")
  }


}
