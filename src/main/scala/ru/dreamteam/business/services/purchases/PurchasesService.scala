package ru.dreamteam.business.services.purchases

import zio.IO

import scala.concurrent.Future

trait PurchasesService[F[_]] {
  def get(): F[String]
  def add()
}
