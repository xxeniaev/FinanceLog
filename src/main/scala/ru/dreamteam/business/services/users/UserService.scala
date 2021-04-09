package ru.dreamteam.business.services.users

import ru.dreamteam.business._

trait UserService[F[_]] {
  def reg(): User
  def login(): User

//  def login(): F[String]
  // что такое F[String]?

//  def reg[login: String]: User = User(login, token)
//  def login[login: String]: User = User(login, token)
  // вопрос: нужно ли пользователю хранить логин? или достаточно ему знать лишь о своем токене?
}
