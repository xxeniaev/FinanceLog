package ru.dreamteam.business.handlers.user.handlers

import ru.dreamteam.business.{User}
import ru.dreamteam.business.handlers.user.{LoginRequest, LoginResponse, PersonalInfoRequest, PersonalInfoResponse, RegistrationRequest, RegistrationResponse}
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.infrastructure.{MainEnv, MainTask}
import zio.ZIO

object PersonalInfoHandler {

  def apply[R](
    userService: UserService[MainTask]
  )(req: PersonalInfoRequest): ZIO[MainEnv, Throwable, PersonalInfoResponse] =
    userService.userInfo(User.Id(req.userId)).map(info => PersonalInfoResponse(info))

}

object RegistrationHandler {

  def apply[R](
    userService: UserService[MainTask]
  )(req: RegistrationRequest): ZIO[MainEnv, Throwable, RegistrationResponse] =
    userService.registration(User.Login(req.login), User.Password(req.password)).map(user =>
      RegistrationResponse(user)
    )

}

object LoginHandler {

  def apply[R](
    userService: UserService[MainTask]
  )(req: LoginRequest): ZIO[MainEnv, Throwable, LoginResponse] =
    userService.login(User.Login(req.login), User.Password(req.password)).map(token =>
      LoginResponse(token)
    )

}
