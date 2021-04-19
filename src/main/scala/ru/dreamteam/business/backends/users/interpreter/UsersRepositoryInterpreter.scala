package ru.dreamteam.business.backends.users.interpreter

import cats.Monad
import cats.effect.BracketThrow
import doobie.h2.H2Transactor
import doobie.implicits.toSqlInterpolator
import ru.dreamteam.business.{User}
import ru.dreamteam.business.backends.users.UsersRepository
import ru.dreamteam.business.backends.purchases.interpreter.PurchaseRepositoryInterpreter.{PurchaseRaw, selectByUserId, transform}
import ru.dreamteam.business.backends.users.UsersRepository.UserReq
import ru.dreamteam.business.backends.users.interpreter.UsersRepositoryInterpreter.{add, selectAll, selectUser}


class UsersRepositoryInterpreter[F[_]: BracketThrow: Monad](transactor: H2Transactor[F]) extends UsersRepository[F] {
  override def findAll(): F[List[User]] =
    for {
      raw <- selectAll().transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  // что-то снова с транзактом - не понятно, потому что BracketThrow на месте. или еще что-то нужно?
  override def findUser(userId: User.Id): F[Option[User]] =
    for {
      raw <- selectUser(userId.id).transact(transactor)
      result = raw.flatMap(transform)
    } yield result

  // методом add получаем строку - id юзера -> делаем Id-тип из строки -> возвращаем его
  // надо подумать возвращать юзера или его id - пока возвращается id
  // и вообще может переписать как-то, но так работает вроде
  override def addUser(user: UserReq): F[User.Id] = {
    for {
      raw <- add(user.login, user.password).transact(transactor).map { id => user.copy(id) }
      id <- add(user.login, user.password)
      result = User.Id(id.toString)
    } yield result
//    sql"INSERT INTO users (login, password) VALUES (${user.login}, ${user.password})".
//      update
//      .withUniqueGeneratedKeys[Long]("id")
//      .transact(transactor).map { id =>
//      user.copy(id)
//    }
  }
}

object UsersRepositoryInterpreter {
  // надо будет переписать первую строчку for comp.
  def transform(raw: UserRaw): Option[User] =
    for {
      id <- raw.userId
      login <- raw.login
      password <- raw.password
    } yield User(User.Id(id), User.Login(login.toString), User.Password(password.toString))

  def selectAll(): doobie.ConnectionIO[List[UserRaw]] =
    sql"SELECT userId, login, password FROM users"
      .query[UserRaw]
      .to[List]

  def selectUser(userId: String): doobie.ConnectionIO[Option[UserRaw]] =
    sql"SELECT login, password FROM users WHERE userId = $userId"
        .query[UserRaw]
        .option

  // тут надо дописать функцию, чтобы строку возвращала нормально
  def add(login: String, password: String): String = {
    sql"INSERT INTO users (login, password) VALUES (${login}, ${password})"
      .update
      .withUniqueGeneratedKeys[Long]("id")
    ???
  }


  case class UserRaw(
                        userId: Long,
                        login: String,
                        password: String
                        )
}
