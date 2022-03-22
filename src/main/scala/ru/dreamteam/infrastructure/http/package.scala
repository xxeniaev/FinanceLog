package ru.dreamteam.infrastructure

import cats.syntax.all._
import ru.dreamteam.business.{Token, User}
import ru.dreamteam.business.services.session.SessionService
import sttp.tapir.{header, Endpoint}
import sttp.tapir.server.ServerEndpoint
import zio.{Has, IO, Task, UIO, ZIO, ZLayer}

// попробуйте сами сделать метод, который достает хедер авторизационный, проверяет сессию, достает по ней пользователя
package object http {

  case object InvalidToken extends RuntimeException

  // подшаманить с типами
  implicit class EndpointReacher2[I, O](endpoint: Endpoint[I, Unit, Response[O], Any]) {

    def handleWithAuthorization(
      logic: I => User.Id => ZIO[MainEnv, Throwable, O]
    )(
      implicit
      sessionService: SessionService[ZIO[MainEnv, Throwable, O]]
    ): ServerEndpoint[(I, String), Unit, Response[O], Any, Task] =
      ServerEndpoint[(I, String), Unit, Response[O], Any, Task](
        endpoint.in(header[String]("session")),
        _ => {
          case (i, token) => {
            val r: ZIO[MainEnv, Nothing, Response[O]] = for {
              trackingId <- ZIO.access[MainEnv](_.get[Context].trackingId)
              idF         = sessionService.getUser(Token(token)).fold(Task.fail(InvalidToken), identity)
              a          <- idF.flatMap(logic(i))
                              .either.catchAllDefect(th => UIO(Left(th)))
                              .map {
                                case Left(value)  => Response(
                                    none[O],
                                    trackingId,
                                    Status.Error,
                                    ErrorPayload(value.getMessage).some
                                  )
                                case Right(value) =>
                                  Response(value.some, trackingId, Status.Ok, none[ErrorPayload])
                              }

              // взять заголовок, где лежит сессия, проверить что сессия у нас есть (SessionService), вернуть юзерайди пользователя
              // если нет сессии отбить ошибкой

            } yield a

            r.map(_.asRight[Unit]).provideLayer(ZLayer.fromFunction(_ =>
              Context(TrackingIdGenerator.generate())
            ))
          }
        }
      )

  }

  implicit class EndpointReacher[I, O](endpoint: Endpoint[I, Unit, Response[O], Any]) {

    def handle(
      logic: I => ZIO[MainEnv, Throwable, O]
    ): ServerEndpoint[I, Unit, Response[O], Any, Task] =
      ServerEndpoint[I, Unit, Response[O], Any, Task](
        endpoint,
        _ =>
          i => {
            val r: ZIO[MainEnv, Nothing, Response[O]] = for {
              trackingId <- ZIO.access[MainEnv](_.get[Context].trackingId)
              a          <- logic(i).either
                              .catchAllDefect(th => UIO(Left(th)))
                              .map {
                                case Left(value)  => Response(
                                    none[O],
                                    trackingId,
                                    Status.Error,
                                    ErrorPayload(value.getMessage).some
                                  )
                                case Right(value) =>
                                  Response(value.some, trackingId, Status.Ok, none[ErrorPayload])
                              }
            } yield a

            r.map(_.asRight[Unit]).provideLayer(ZLayer.fromFunction(_ =>
              Context(TrackingIdGenerator.generate())
            ))
          }
      )

  }

}
