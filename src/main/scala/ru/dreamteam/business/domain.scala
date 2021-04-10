package ru.dreamteam.business

import io.estatico.newtype.macros.newtype


case class App()

case class CurrencyName(name: String)
case class CurrencyCode(name: String)
case class Currency(name: CurrencyName, code: CurrencyCode)
case class Money(amount:BigDecimal, currency: Currency)

case class Token(token: String)
case class Login(login: String)
case class Password(value: String)


case class User(userId: User.Id, login: Login, password: Password)
// new type usage
object User {
  @newtype case class Id(id: String)
}


case class PurchaseType(t: String)
case class Purchase(money: Money, comment: String, `type`: PurchaseType)
