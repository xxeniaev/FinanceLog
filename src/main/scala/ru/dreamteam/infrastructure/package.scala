package ru.dreamteam

import zio.{Has, ZEnv, ZIO}

package object infrastructure {

  type MainEnv = Has[Context] //with ZEnv

  type MainTask[A] = ZIO[MainEnv, Throwable, A]
}
