package ru.dreamteam.business.services.users

import ru.dreamteam.business._
import zio.IO
import cats.effect.Resource
import scala.concurrent.Future

trait UserService[F[_]] {
  def login(login: User.Login, password: User.Password): F[Token]
  def registration(login: User.Login, password: User.Password): F[User] // проверили что такого нет, записали, вернули созданного
  def userInfo(): F[String]
}
