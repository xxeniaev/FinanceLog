package ru.dreamteam.business.services.session

import ru.dreamteam.business.{Token, User}

trait SessionService[F[_]] {
  def generate(user: User): F[Token]
  def getUser(token: Token): F[Option[User.Id]]
}