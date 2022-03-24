package ru.dreamteam.business.repository.users

import ru.dreamteam.business.User
import ru.dreamteam.business.repository.users.UsersRepository.UserReq

trait UsersRepository[F[_]] {
  def findUser(userId: User.Id): F[Option[User]]
  def findUserByLogin(userLogin: User.Login): F[Option[User]]
  def addUser(user: UserReq): F[User.Id]
}

object UsersRepository {
  case class UserReq(login: User.Login, password: User.Password)
}
