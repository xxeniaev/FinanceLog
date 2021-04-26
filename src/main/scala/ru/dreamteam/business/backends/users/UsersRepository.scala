package ru.dreamteam.business.backends.users

import ru.dreamteam.business.User
import ru.dreamteam.business.backends.users.UsersRepository.UserReq

trait UsersRepository[F[_]] {
  def addUser(user: UserReq):F[User]
  def getUser(userId: User.Id): F[Option[User]]
  def getUserByLogin(login: User.Login): F[Option[User]]
}
object UsersRepository {
  case class UserReq(login: String, password: String)
}

