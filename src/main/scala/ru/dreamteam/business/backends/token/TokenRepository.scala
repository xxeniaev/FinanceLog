package ru.dreamteam.business.backends.token

import ru.dreamteam.business.{Token, User}

trait TokenRepository[F[_]] {
  def login(userId: User.Id): Token  // generates token
  def logout(userId: User.Id) // delete token
}
