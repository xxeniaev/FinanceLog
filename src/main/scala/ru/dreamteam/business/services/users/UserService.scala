package ru.dreamteam.business.services.users

trait UserService[F[_]] {
  def login(): F[String]
}
