package ru.dreamteam.business.services.users

import ru.dreamteam.business._
import ru.dreamteam.business.services.users.interpreter.UserInfo

trait UserService[F[_]] {
  def login(login: User.Login, password: User.Password): F[Token]

  def registration(
    login: User.Login,
    password: User.Password
  ): F[User]

  def userInfo(userId: User.Id): F[UserInfo]
}
