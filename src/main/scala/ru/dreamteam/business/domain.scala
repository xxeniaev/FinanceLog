package ru.dreamteam.business

import derevo.derive
import derevo.tethys.{tethysReader, tethysWriter}
import enumeratum.{Enum, EnumEntry}
import io.estatico.newtype.macros.newtype
import ru.dreamteam.business.Currency.XXX
import ru.dreamteam.business.Purchase.PurchaseCategory

case class App()

case class Token(token: String)

@derive(tethysReader, tethysWriter)
case class User(userId: User.Id, login: User.Login, password: User.Password)

case class Money(amount: BigDecimal, currency: Currency)

// new type usage

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

  object PurchaseCategory extends Enum[PurchaseCategory] {
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

object Currency extends Enum[Currency] {
  val values = findValues

  case object RUB extends Currency
  case object USD extends Currency
  case object XXX extends Currency

  def parse(str: String): Currency = Currency.withNameInsensitiveOption(str).getOrElse(XXX)
}
