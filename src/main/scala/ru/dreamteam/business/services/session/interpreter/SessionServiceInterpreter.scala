package ru.dreamteam.business.services.session.interpreter

import ru.dreamteam.business.{Token, User}
import ru.dreamteam.business.services.session.SessionService


// read from cats
// ref MVAR
class SessionServiceInterpreter[F[_]]() extends SessionService[F] {

  override def generate(login: User.Login): F[Token] = ???
  override def getUser(token: Token): F[User.Id] = ???

}
