package ru.dreamteam.business.services.users.interpreter

import ru.dreamteam.business.User
import ru.dreamteam.business.services.users.UserService

class UserServiceInterpreter() extends UserService {
  override def login(): User = ???

  override def reg(): User = ???
}
