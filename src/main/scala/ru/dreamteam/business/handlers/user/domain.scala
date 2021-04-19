package ru.dreamteam.business.handlers.user

import derevo.derive
import derevo.tethys._
import ru.dreamteam.business.User

case class PersonalInfoRequest(userId: String)

@derive(tethysReader, tethysWriter)
case class PersonalInfoResponse(name: String)