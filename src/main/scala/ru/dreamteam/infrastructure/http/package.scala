package ru.dreamteam.infrastructure

import cats.syntax.all._
import sttp.tapir.Endpoint
import sttp.tapir.server.ServerEndpoint
import tethys.JsonWriterOps
import tethys.commons.RawJson
import tethys.jackson._
import zio.{Has, IO, Task, UIO, ZIO, ZLayer}

package object http {

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
              a <- logic(i).either
                .catchAllDefect(th => UIO(Left(th)))
                .flatMap {
                  case Left(value) =>
                    UIO(Response(none[O], trackingId, Status.Error, ErrorPayload(value.getMessage).some))
                  case Right(value) =>
                    UIO(Response(value.some, trackingId, Status.Ok, none[ErrorPayload]))
                }
            } yield a

            r.map(_.asRight[Unit]).provideLayer(ZLayer.fromFunction(_ => Context(TrackingIdGenerator.generate())))
          }
      )
  }
}
