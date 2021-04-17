package ru.dreamteam.business

import enumeratum.{Enum, EnumEntry}
import io.estatico.newtype.macros.newtype

case class App()

case class Currency(name: Currency.CurrencyName, code: Currency.CurrencyCode)

object Currency {
  @newtype case class CurrencyName(name: String)
  @newtype case class CurrencyCode(name: String)
}


case class Token(token: String)
case class User(userId: User.Id, login: User.Login, password: User.Password)
case class Money(amount: BigDecimal, currency: Currency)
// new type usage
object User {
  // 0. вроде при добавлении в бд генерируется id для обращения в бд, нужен ли он здесь ?
  // id брать из бд
  @newtype case class Id(id: String)
  @newtype case class Login(login: String)
  @newtype case class Password(token: String)
}


case class Purchase(purchaseId: Purchase.Id, money: Money, comment: Purchase.Comment, category: Purchase.Category)

object Purchase {
  // 0.1 и туть тоже
  @newtype case class Id(id: String)
  @newtype case class Comment(comment: String)
  @newtype case class Category(category: PurchaseType) // лишняя вложенность убрать


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
}

//case object PurchaseNotFoundError
//case object UserNotFoundError
