package ru.dreamteam.business.services.session

import ru.dreamteam.business.Token

trait SessionService[F[_]] {
  def generate(): F[Token]
}