package ru.dreamteam.business

import derevo.derive
import derevo.tethys.{tethysReader, tethysWriter}
import enumeratum.{Enum, EnumEntry}
import io.estatico.newtype.macros.newtype
import ru.dreamteam.business.Purchase.PurchaseCategory
import sttp.tapir.codec.newtype._
import ru.dreamteam.infrastructure.newtype._
import tethys.enumeratum.TethysEnum


@derive(tethysReader, tethysWriter)
case class Token(token: String)

@derive(tethysReader, tethysWriter)
case class User(userId: User.Id, login: User.Login, password: User.Password)

@derive(tethysReader, tethysWriter)
case class Money(amount: BigDecimal, currency: Currency)

object User {
  @newtype case class Id(id: Int)
  @newtype case class Login(login: String)
  @newtype case class Password(password: String)
}

@derive(tethysReader, tethysWriter)
case class Purchase(
  purchaseId: Purchase.Id,
  money: Money,
  comment: Purchase.Comment,
  category: PurchaseCategory
)

object Purchase {
  @newtype case class Id(id: Int)
  @newtype case class Comment(comment: String)

  sealed trait PurchaseCategory extends EnumEntry

  object PurchaseCategory extends Enum[PurchaseCategory] with TethysEnum[PurchaseCategory] {
    val values = findValues

    case object MARKET    extends PurchaseCategory
    case object TRANSPORT extends PurchaseCategory
    case object CAFE      extends PurchaseCategory
    case object SPORT     extends PurchaseCategory
    case object NECESSARY extends PurchaseCategory
    case object OTHER     extends PurchaseCategory

    def parse(str: String): PurchaseCategory =
      PurchaseCategory.withNameInsensitiveOption(str).getOrElse(OTHER)

  }

}

sealed trait Currency extends EnumEntry

object Currency extends Enum[Currency] with TethysEnum[Currency] {
  val values = findValues

  case object RUB extends Currency
  case object USD extends Currency
  case object XXX extends Currency

  def parse(str: String): Currency = Currency.withNameInsensitiveOption(str).getOrElse(XXX)
}
