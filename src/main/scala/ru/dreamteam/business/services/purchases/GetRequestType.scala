package ru.dreamteam.business.services.purchases
// думаем, что было бы хорошо уметь получать покупки по типу запроса, но не знаем
// как можно поступить с сигнатурой передаваемых данных

import enumeratum.EnumEntry
import enumeratum._
import ru.dreamteam.business.Purchase.PurchaseType

sealed trait RequestType extends EnumEntry
object Nesting extends Enum[PurchaseType] {
  val values = findValues

  case object ALL extends RequestType
  case object TIME extends RequestType
  case object MONEY extends RequestType
  case object TYPE extends RequestType
}