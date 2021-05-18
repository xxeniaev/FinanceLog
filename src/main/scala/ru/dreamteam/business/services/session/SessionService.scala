package ru.dreamteam.business.services.session

import ru.dreamteam.business.{Token, User}

trait SessionService[F[_]] {
  def generate(login: User.Id): F[Token]
  def getUserId(token: Token): F[Option[User.Id]]
  def removeToken(token: Token): F[Unit]
//  def addTokenUser(token: Token, user: User): F[Unit]
}