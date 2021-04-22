package ru.dreamteam.business.services.session

import ru.dreamteam.business.{Token, User}

trait SessionService[F[_]] {
  def generate(login: User.Login): F[Token]
  def getUser(token: Token): F[User.Id]
}