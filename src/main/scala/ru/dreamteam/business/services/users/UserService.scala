package ru.dreamteam.business.services.users

import ru.dreamteam.business._
import zio.IO
import cats.effect.Resource
import scala.concurrent.Future

trait UserService[F[_]] {
  def login(login: User.Login, password: User.Password): Option[F[Token]]
  def registration(login: User.Login, password: User.Password): Option[F[User]]
  /*Д: что делать, если существует пользователь с таким именем? Пока сделал, что возвращает None*/
}
