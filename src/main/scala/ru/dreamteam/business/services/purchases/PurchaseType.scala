package ru.dreamteam.business.services.purchases

sealed trait PurchaseType
case object MARKET extends PurchaseType
case object TRANSPORT extends PurchaseType
case object CAFE extends PurchaseType
case object SPORT extends PurchaseType
case object NECESSARY extends PurchaseType
case object OTHER extends PurchaseType