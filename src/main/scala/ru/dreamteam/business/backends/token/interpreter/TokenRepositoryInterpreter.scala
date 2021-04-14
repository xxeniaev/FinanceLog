package ru.dreamteam.business.backends.token.interpreter

import doobie.h2.H2Transactor
import ru.dreamteam.business.{Token, User}
import ru.dreamteam.business.backends.token.TokenRepository

class TokenRepositoryInterpreter [F[_]](transactor: H2Transactor[F]) extends TokenRepository[F]{
  override def login(userId: User.Id): Token = ???

  override def logout(userId: User.Id): Unit = ???
}
