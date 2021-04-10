package ru.dreamteam.business.backends.users

import ru.dreamteam.business.User

trait UsersRepository[F[_]] {
  def addUser()
  def getUser(): F[User]
}

class UsersRepositoryInterpreter[F[_]]() extends UsersRepository[F] {
  override def addUser(): Unit = ???

  override def getUser(): F[User] = ???
}
