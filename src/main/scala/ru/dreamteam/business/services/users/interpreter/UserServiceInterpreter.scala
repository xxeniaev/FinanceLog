package ru.dreamteam.business.services.users.interpreter

import cats.effect.Sync
import ru.dreamteam.business.backends.users.UsersRepository
import ru.dreamteam.business.backends.users.UsersRepository.UserReq
import ru.dreamteam.business.services.session.SessionService
import ru.dreamteam.business.services.users.UserService
import ru.dreamteam.business.{Token, User}

class UserServiceInterpreter[F[_]: Sync](sessionService: SessionService[F], repo: UsersRepository[F]) extends UserService[F] {
  override def login(login: User.Login, password: User.Password): Option[F[Token]] = {
    for {
      /* Д: пояаились вопросы по UserRepository, а именно насчет метода getUser */
      fromBd <- repo.getUserIdByData(login, password) //получаем id, если login и пароль совпадают
      //token <- sessionService.generate(fromBd, login) //не могу достать из F[User.Id] User.Id
    } yield Sync[F].delay { Token(???) }
  }

  override def registration(login: User.Login, password: User.Password): Option[F[User]] = repo.isExistUser(login) match {
    case true => None
    case false => Some(repo.addUser(UserReq(login.login, password.token)))
  }
}
