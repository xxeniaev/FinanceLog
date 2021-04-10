package ru.dreamteam.business.backends.users

import ru.dreamteam.business.User

trait UsersRepository[F[_]] {
  def addUser(user: User)
  def getUser(userId: User.Id): F[User]
}

