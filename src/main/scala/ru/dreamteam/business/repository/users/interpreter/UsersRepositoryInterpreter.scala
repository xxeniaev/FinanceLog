package ru.dreamteam.business.repository.users.interpreter

import cats.Monad
import cats.syntax.all._
import cats.effect.BracketThrow
import doobie.h2.H2Transactor
import ru.dreamteam.business.User
import doobie.implicits._
import ru.dreamteam.business.repository.users.UsersRepository
import ru.dreamteam.business.repository.users.UsersRepository.UserReq
import ru.dreamteam.business.repository.users.interpreter.UsersRepositoryInterpreter.{insertUser, selectUser, selectUserByLogin, transform}
import doobie.implicits.toSqlInterpolator
import doobie.util.transactor.Transactor

class UsersRepositoryInterpreter[F[_]: BracketThrow: Monad](transactor: Transactor[F])
  extends UsersRepository[F] {

  override def findUser(userId: User.Id): F[Option[User]] = for {
    raw   <- selectUser(userId.id).transact(transactor)
    result = raw.flatMap(transform)
  } yield result

  override def addUser(user: UserReq): F[User.Id] = for {
    id <- insertUser(user.login.login, user.password.password).transact(transactor)
  } yield User.Id(id)

  override def findUserByLogin(userLogin: User.Login): F[Option[User]] = for {
    raw   <- selectUserByLogin(userLogin.login).transact(transactor)
    result = raw.flatMap(transform)
  } yield result
}

object UsersRepositoryInterpreter {

  def transform(raw: UserRaw): Option[User] =
    Option(User(User.Id(raw.userId), User.Login(raw.login), User.Password(raw.password)))

  def selectUser(userId: Int): doobie.ConnectionIO[Option[UserRaw]] =
    sql"SELECT login, password FROM users WHERE userId = $userId"
      .query[UserRaw]
      .option

  def selectUserByLogin(userLogin: String): doobie.ConnectionIO[Option[UserRaw]] =
    sql"SELECT userId, password FROM users WHERE login = $userLogin"
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
