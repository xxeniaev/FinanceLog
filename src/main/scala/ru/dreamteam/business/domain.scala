package ru.dreamteam.business


case class A()

case class CurrencyName(name: String)
case class CurrencyCode(name: String)
case class Currency(name: CurrencyName, code: CurrencyCode)
case class Money(amount:BigDecimal, currency: Currency)


