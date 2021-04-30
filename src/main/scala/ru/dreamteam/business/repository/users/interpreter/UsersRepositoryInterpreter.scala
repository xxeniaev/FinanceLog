package ru.dreamteam.business.repository.users.interpreter

import cats.Monad
import cats.syntax.all._
import cats.effect.BracketThrow
import doobie.h2.H2Transactor
import ru.dreamteam.business.User
import doobie.implicits._
import ru.dreamteam.business.repository.users.UsersRepository
import ru.dreamteam.business.repository.users.UsersRepository.UserReq
import ru.dreamteam.business.repository.users.interpreter.UsersRepositoryInterpreter.{insertUser, selectAll, selectUser, transform}
import doobie.implicits.toSqlInterpolator

class UsersRepositoryInterpreter[F[_]: BracketThrow: Monad](transactor: H2Transactor[F])
  extends UsersRepository[F] {

  override def findUser(userId: User.Id): F[Option[User]] = for {
    raw   <- selectUser(userId.id).transact(transactor)
    result = raw.flatMap(transform)
  } yield result

  override def addUser(user: UserReq): F[User.Id] = for {
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

  def selectUser(userId: Int): doobie.ConnectionIO[Option[UserRaw]] =
    sql"SELECT login, password FROM users WHERE userId = $userId"
      .query[UserRaw]
      .option

  def insertUser(login: String, password: String): doobie.ConnectionIO[Int] =
    sql"INSERT INTO users (login, password) VALUES ($login, $password)"
      .update
      .withUniqueGeneratedKeys[Int]("userId")

  case class UserRaw(
    userId: Int,
    login: String,
    password: String
  )

}
