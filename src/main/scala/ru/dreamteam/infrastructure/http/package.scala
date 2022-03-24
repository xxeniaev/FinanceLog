package ru.dreamteam.infrastructure

import cats.syntax.all._
import ru.dreamteam.business.{Token, User}
import ru.dreamteam.business.services.session.SessionService
import sttp.tapir.{header, Endpoint}
import sttp.tapir.server.ServerEndpoint
import zio.{Has, IO, Task, UIO, ZIO, ZLayer}

package object http {

  case object InvalidToken extends RuntimeException

  implicit class EndpointReacher2[I, O](endpoint: Endpoint[I, Unit, Response[O], Any]) {

    type T[A] = ZIO[MainEnv, Throwable, A]

    def handleWithAuthorization(
      logic: I => User.Id => T[O]
    )(
      implicit
      sessionService: SessionService[T]
    ): ServerEndpoint[(I, String), Unit, Response[O], Any, Task] =
      ServerEndpoint[(I, String), Unit, Response[O], Any, Task](
        endpoint.in(header[String]("session")),
        _ => {
          case (i, token) => {
            val r = for {
              trackingId <- ZIO.access[MainEnv](_.get[Context].trackingId)
              r          <- sessionService.getUser(Token(token)).flatMap(userId =>
                              logic(i)(userId.get)
                            ) // <- fix this unsafe get operation
                              .either
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
            } yield r

            r
              .map(_.asRight[Unit]).provideLayer(ZLayer.fromFunction(_ =>
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
