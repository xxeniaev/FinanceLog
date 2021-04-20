package ru.dreamteam.business.backends.users.interpreter

import cats.Monad
import cats.syntax.all._
import cats.effect.BracketThrow
import doobie.h2.H2Transactor
import ru.dreamteam.business.User
import doobie.implicits._
import ru.dreamteam.business.backends.users.UsersRepository
import ru.dreamteam.business.backends.users.UsersRepository.UserReq
import ru.dreamteam.business.backends.users.interpreter.UsersRepositoryInterpreter.{transform, insertUser, selectAll, selectUser}
import doobie.implicits.toSqlInterpolator


class UsersRepositoryInterpreter[F[_]: BracketThrow: Monad](transactor: H2Transactor[F]) extends UsersRepository[F] {
  override def findAll(): F[List[User]] =
    for {
      raw <- selectAll().transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  override def findUser(userId: User.Id): F[Option[User]] =
    for {
      raw <- selectUser(userId.id).transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  override def addUser(user: UserReq): F[User.Id] =
    for {
      id <- insertUser(user.login.login, user.password.password).transact(transactor)
    } yield User.Id(id)
}

object UsersRepositoryInterpreter {
  def transform(raw: UserRaw): Option[User] =
    Option(User(User.Id(raw.userId), User.Login(raw.login), User.Password(raw.password)))

  def selectAll(): doobie.ConnectionIO[List[UserRaw]] =
    sql"SELECT userId, login, password FROM users"
      .query[UserRaw]
      .to[List]

  def selectUser(userId: String): doobie.ConnectionIO[Option[UserRaw]] =
    sql"SELECT login, password FROM users WHERE userId = $userId"
        .query[UserRaw]
        .option

  def insertUser(login: String, password: String): doobie.ConnectionIO[String] = {
    sql"INSERT INTO users (login, password) VALUES ($login, $password)"
      .update
      .withUniqueGeneratedKeys[String]("userId")
  }

  case class UserRaw(
                        userId: String,
                        login: String,
                        password: String
                        )
}
