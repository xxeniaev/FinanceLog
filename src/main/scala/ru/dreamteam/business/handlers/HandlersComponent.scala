package ru.dreamteam.business.handlers

import cats.effect.Resource
import ru.dreamteam.business.handlers.system.SystemModule

case class HandlersComponent()
object HandlersComponent {
  def build[F[_]](runtime: zio.Runtime[Unit]): Resource[F, HandlersComponent] = {

//    val system = new SystemModule(runtime)
//
//    for {
//
//    } yield ()
    ???
  }
}
