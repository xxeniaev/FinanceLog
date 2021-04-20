package ru.dreamteam.business.handlers.user

import derevo.derive
import derevo.tethys._
import ru.dreamteam.business.User
import sttp.tapir.description

case class PersonalInfoRequest(userId: String)

@derive(tethysReader, tethysWriter)
@description("Информации об пользователе")
case class PersonalInfoResponse(
  @description("Имя пользователя") name: String
)
