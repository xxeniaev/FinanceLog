package ru.dreamteam.business.backends.users.interpreter

import doobie.h2.H2Transactor
import doobie.implicits.toSqlInterpolator
import ru.dreamteam.business.{Purchase, User}
import ru.dreamteam.business.backends.users.UsersRepository

import scala.concurrent.Future


class UsersRepositoryInterpreter[F[_]](transactor: H2Transactor[F]) extends UsersRepository[F] {
  override def addUser(user: User): Unit = Future[Purchase] = {
    sql"INSERT INTO users (login, password) VALUES (${user.login}, ${user.password})".update.withUniqueGeneratedKeys[Long]("id").transact(transactor).map { id =>
      user.copy(id)
    }
  }

  // 0. тут доделать, переделать без Either
  // 1. почему transact не работает

  // 2. в таких случаях надо F[User] или Future[User] ?
  override def getUser(userId: User.Id): F[User] = {
    sql"SELECT * FROM users WHERE userId = $userId".query[User].option.transact(transactor).map {
      case Some(user) => user
      case _ => None
    }
  }

  override def getUser(login: User.Login): F[User] = {
    sql"SELECT * FROM users WHERE login = $login".query[User].option.transact(transactor).map {
      case Some(user) => user
      case _ => None
    }
  }
}