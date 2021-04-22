package ru.dreamteam.business

import enumeratum.{Enum, EnumEntry}
import io.estatico.newtype.macros.newtype
import ru.dreamteam.business.Purchase.{PurchaseType}

case class App()



case class Token(token: String)
case class User(userId: User.Id, login: User.Login, password: User.Password)
case class Money(amount: BigDecimal, currency: Currency)
// new type usage

object User {
  @newtype case class Id(id: String)
  @newtype case class Login(login: String)
  @newtype case class Password(password: String)
}


case class Purchase(purchaseId: Purchase.Id, money: Money, comment: Purchase.Comment, category: PurchaseType)

object Purchase {
  @newtype case class Id(id: String)
  @newtype case class Comment(comment: String)


  sealed trait PurchaseType extends EnumEntry
  object PurchaseType extends Enum[PurchaseType] {
    val values = findValues

    case object MARKET extends PurchaseType
    case object TRANSPORT extends PurchaseType
    case object CAFE extends PurchaseType
    case object SPORT extends PurchaseType
    case object NECESSARY extends PurchaseType
    case object OTHER extends PurchaseType
  }


}


sealed trait Currency extends EnumEntry
object Currency extends Enum[Currency] {
  val values = findValues

  case object RUB extends Currency
  case object USD extends Currency
  case object XXX extends Currency

  def parse(str: String): Currency =
    Currency.withNameInsensitiveOption(str).getOrElse(XXX)
}