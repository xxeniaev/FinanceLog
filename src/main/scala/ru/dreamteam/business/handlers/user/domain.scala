package ru.dreamteam.business.handlers.user

import derevo.derive
import derevo.tethys._
import sttp.tapir.description

case class PersonalInfoRequest(userId: Int)

@derive(tethysReader, tethysWriter)
@description("Информация о пользователе")
case class PersonalInfoResponse(
  @description("Имя пользователя") name: String
)

case class RegistrationRequest(login: String, password: String)

@derive(tethysReader, tethysWriter)
@description("Регистрация пользователя")
case class RegistrationResponse(
  @description("Идентификатор пользователя") id: Int
)

case class LoginRequest(login: String, password: String)

@derive(tethysReader, tethysWriter)
@description("Вход пользователя")
case class LoginResponse(
  @description("Идентификатор пользователя") id: Int
)
