package ru.dreamteam.business

import io.estatico.newtype.macros.newtype
import ru.dreamteam.business.services.purchases.PurchaseType


case class App()

case class Currency(name: Currency.CurrencyName, code: Currency.CurrencyCode)

object Currency {
  @newtype case class CurrencyName(name: String)
  @newtype case class CurrencyCode(name: String)
}


case class Token(token: String)
case class User(userId: User.Id, login: User.Login, password: User.Password)

// new type usage
object User {
  // 0. вроде при добавлении в бд генерируется id для обращения в бд, нужен ли он здесь ?
  // id брать из бд
  @newtype case class Id(id: String)
  @newtype case class Login(login: String)
  @newtype case class Password(token: String)
}


case class Purchase(purchaseId: Purchase.Id, money: Purchase.Money, comment: Purchase.Comment, category: Purchase.Category)

object Purchase {
  // 0.1 и туть тоже
  @newtype case class Id(id: String)
  @newtype case class Money(amount:BigDecimal, currency: Currency)
  @newtype case class Comment(comment: String)
  @newtype case class Category(category: PurchaseType)
}

//case object PurchaseNotFoundError
//case object UserNotFoundError
