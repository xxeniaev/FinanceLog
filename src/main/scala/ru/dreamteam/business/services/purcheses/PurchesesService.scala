package ru.dreamteam.business.services.purcheses

import zio.IO

import scala.concurrent.Future

trait PurchesesService[F[_]] {
  def getAll(): F[String]
}
