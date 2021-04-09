package ru.dreamteam.business.services.purchases
// думаем, что было бы хорошо уметь получать покупки по типу запроса, но не знаем
// как можно поступить с сигнатурой передаваемых данных

sealed trait RequestType
case object ALL extends RequestType
case object TIME extends RequestType
case object MONEY extends RequestType
case object TYPE extends RequestType
