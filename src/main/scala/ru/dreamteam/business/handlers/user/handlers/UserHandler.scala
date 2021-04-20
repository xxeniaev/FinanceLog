package ru.dreamteam.business.handlers.user.handlers

import ru.dreamteam.business.handlers.user.{PersonalInfoRequest, PersonalInfoResponse}
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.infrastructure.{Context, MainEnv, MainTask}
import zio.ZIO

object UserHandler {

  def apply[R](
    userService: UserService[MainTask]
  )(req: PersonalInfoRequest): ZIO[MainEnv, Throwable, PersonalInfoResponse] =
    userService.userInfo().map(info => PersonalInfoResponse(info))

}
