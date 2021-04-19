package ru.dreamteam.infrastructure.http

import enumeratum.EnumEntry
import tethys.enumeratum.TethysEnum
import enumeratum.Enum

sealed trait Status extends EnumEntry
object Status extends Enum[Status] with TethysEnum[Status] {

  case object Ok extends Status
  case object Error extends Status

  override def values: IndexedSeq[Status] = findValues
}
