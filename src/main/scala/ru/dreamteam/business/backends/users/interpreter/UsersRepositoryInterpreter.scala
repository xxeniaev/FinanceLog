package ru.dreamteam.business.backends.users.interpreter

import cats.effect.BracketThrow
import doobie.h2.H2Transactor
import doobie.implicits.toSqlInterpolator
import ru.dreamteam.business.{Purchase, User}
import ru.dreamteam.business.backends.users.UsersRepository
import cats.syntax.all._
import ru.dreamteam.business.backends.users.UsersRepository.UserReq


class UsersRepositoryInterpreter[F[_]: BracketThrow](transactor: H2Transactor[F]) extends UsersRepository[F] {
  override def addUser(user: UserReq): F[User] = {
//    sql"INSERT INTO users (login, password) VALUES (${user.login}, ${user.password})".
//      update
//      .withUniqueGeneratedKeys[Long]("id")
//      .transact(transactor).map { id =>
//      user.copy(id)
//    }
    ???
  }

  // 0. тут доделать, переделать без Either
  // 1. почему transact не работает

  // 2. в таких случаях надо F[User] или Future[User] ?
  override def getUser(userId: User.Id): F[Option[User]] = {
//    sql"SELECT login, password FROM users WHERE userId = $userId"
//      .query[User]
//      .option
//      .transact(transactor)
    ???
  }
}