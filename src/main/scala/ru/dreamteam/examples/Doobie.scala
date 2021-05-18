package ru.dreamteam.examples

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.h2._

import scala.concurrent.ExecutionContext

object Doobie extends App {
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)

  // Resource yielding a transactor configured with a bounded connect EC and an unbounded
  // transaction EC. Everything will be closed and shut down cleanly after use.
  val transactor: Resource[IO, H2Transactor[IO]] =
  for {
    ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
    be <- Blocker[IO]    // our blocking EC
    xa <- H2Transactor.newH2Transactor[IO](
      "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", // connect URL
      "sa",                                   // username
      "",                                     // password
      ce,                                     // await connection here
      be                                      // execute JDBC operations here
    )
  } yield xa


  val script: IO[Unit] =
    transactor.use { xa =>

      // Construct and run your server here!
      for {
        _ <- IO(println("да"))
      } yield ()

    }

  script.unsafeRunSync()
}
