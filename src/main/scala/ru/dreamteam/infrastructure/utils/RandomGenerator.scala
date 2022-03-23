package ru.dreamteam.infrastructure.utils

import cats.effect.Sync

import scala.util.Random

trait RandomGenerator[F[_]] {
  def generateRandomString(length: Int): F[String]
}

class RandomGeneratorImpl[F[_]: Sync] extends RandomGenerator[F] {
  private val random = new Random()

  override def generateRandomString(length: Int): F[String] =
    Sync[F].delay(random.alphanumeric.take(length).mkString)

}
