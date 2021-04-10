package ru.dreamteam.business.services.purchases

import enumeratum.EnumEntry
import enumeratum._

sealed trait PurchaseType extends EnumEntry
object Nesting extends Enum[PurchaseType] {
  val values = findValues

  case object MARKET extends PurchaseType
  case object TRANSPORT extends PurchaseType
  case object CAFE extends PurchaseType
  case object SPORT extends PurchaseType
  case object NECESSARY extends PurchaseType
  case object OTHER extends PurchaseType
}