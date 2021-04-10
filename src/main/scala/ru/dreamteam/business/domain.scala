package ru.dreamteam.business

import io.estatico.newtype.macros.newtype


case class App()

case class CurrencyName(name: String)
case class CurrencyCode(name: String)
case class Currency(name: CurrencyName, code: CurrencyCode)
case class Money(amount:BigDecimal, currency: Currency)


case class Token(token: String)
case class User(userId: User.Id, login: User.Login, password: User.Password)

// new type usage
object User {
  @newtype case class Id(id: String)
  @newtype case class Login(login: String)
  @newtype case class Password(token: String)
}


case class Purchase(money: Money, comment: String, category: Purchase.PurchaseType)

object Purchase {
  @newtype case class Id(id: String)
  @newtype case class PurchaseType(category: String)
}

case object PurchaseNotFoundError
case object UserNotFoundError
