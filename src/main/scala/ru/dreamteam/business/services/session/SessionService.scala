package ru.dreamteam.business.services.session

import ru.dreamteam.business.{Token, User}

trait SessionService[F[_]] {
  def generate(userId: User.Id, userLogin: User.Login): F[Token]
  def getUser(token: Token): F[User.Id]
  def deleteUserToken(token: Token): F[Unit]
}