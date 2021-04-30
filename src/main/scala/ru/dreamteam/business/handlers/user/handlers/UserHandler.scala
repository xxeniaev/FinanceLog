package ru.dreamteam.business.handlers.user.handlers

import ru.dreamteam.business.handlers.user.{PersonalInfoRequest, PersonalInfoResponse}
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.infrastructure.{MainEnv, MainTask}
import zio.ZIO

object UserHandler {

  // тут не понятно в мапе что за info
  def apply[R](
    userService: UserService[MainTask]
  )(req: PersonalInfoRequest): ZIO[MainEnv, Throwable, PersonalInfoResponse] =
    userService.userInfo().map(info => PersonalInfoResponse(info + req.userId))

}
