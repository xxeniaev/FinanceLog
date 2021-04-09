package ru.dreamteam.business


case class App()

case class CurrencyName(name: String)
case class CurrencyCode(name: String)
case class Currency(name: CurrencyName, code: CurrencyCode)
case class Money(amount:BigDecimal, currency: Currency)

case class Token(token: String)
case class Login(login: String)
case class User(login: Login, token: Token)

case class PurchaseType(t: String)
case class Purchase(money: Money, comment: String, t: PurchaseType)
