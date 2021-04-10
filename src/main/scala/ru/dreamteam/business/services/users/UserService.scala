package ru.dreamteam.business.services.users

import ru.dreamteam.business._
import zio.IO
import cats.effect.Resource
import scala.concurrent.Future

trait UserService[F[_]] {
  def login(login: Login, password: Password): F[Token]
  def registration(login: Login, password: Password): F[User] // проверили что такого нет, записали, вернули созданного
}
