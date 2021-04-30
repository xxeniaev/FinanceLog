package ru.dreamteam.business.services.users

import ru.dreamteam.business._
import zio.IO
import cats.effect.Resource
import scala.concurrent.Future

trait UserService[F[_]] {
  def login(login: User.Login, password: User.Password): F[Token]

  def registration(
    login: User.Login,
    password: User.Password
  ): F[User.Id] // проверили что такого нет, записали, вернули созданного

  // почему этот метод не принимает аргументов (юзер ид)?
  // p.s. сделала метод получения инфы по покупке тоже, но он там принимает аргс(юзер ид, пурчейс ид), если что исправлю
  def userInfo(): F[String]
}
