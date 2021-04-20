package ru.dreamteam.infrastructure

import java.util.concurrent.ThreadLocalRandom


object TrackingIdGenerator {
  val alphabet = "QWERTYUPADFGHJKLZXCVBNM1234567890"
  val idLength = 10

  def generate(): String =
    List.fill(idLength)(
      ThreadLocalRandom.current().nextInt(alphabet.length)).map(alphabet).mkString
}
