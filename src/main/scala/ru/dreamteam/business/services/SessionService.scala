package ru.dreamteam.business.services

import ru.dreamteam.business.Token

trait SessionService[F[_]] {
  def generate(): F[Token]
}

// read from cats
// ref MVAR
class SessionServiceInterpreter[F[_]]() extends SessionService[F] {
  override def generate(): F[Token] = ???
}