package ru.dreamteam.business.backends.users

import ru.dreamteam.business.{User}
import ru.dreamteam.business.backends.users.UsersRepository.UserReq

trait UsersRepository[F[_]] {
  def findAll(): F[List[User]]
  def findUser(userId: User.Id): F[Option[User]]
  def addUser(user: UserReq):F[User.Id]
}
object UsersRepository {
  case class UserReq(login: User.Login, password: User.Password)
}

