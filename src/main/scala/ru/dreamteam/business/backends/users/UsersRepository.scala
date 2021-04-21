package ru.dreamteam.business.backends.users

import ru.dreamteam.business.User
import ru.dreamteam.business.backends.users.UsersRepository.UserReq

trait UsersRepository[F[_]] {
  def addUser(user: UserReq):F[User]
  def getUser(userId: User.Id): F[Option[User]]
  /*Д: нужен ли нам такой метод getUser??
  он по id, выдает User с полями login и password, то есть я могу случайно выбрать id, и получить какого-нибудь
  пользователя?? да и где нам вообще может понадобиться password пользователя в программе?
  Предлагаю заменить на getUserIdByData, а в токене таскать id и login
  */
  def getUserIdByData(userLogin: User.Login, userPassword: User.Password): Option[F[User.Id]] /*пусть отдает id,
  только при полном  совпадении */

  def isExistUser(userLogin: User.Login): Boolean //существует ли пользователь с таким логином
}
object UsersRepository {
  case class UserReq(login: String, password: String)
}

