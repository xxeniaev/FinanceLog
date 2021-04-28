package ru.dreamteam.application

import java.util.concurrent.Executors
import cats.effect.{Async, Blocker, ContextShift}
import com.zaxxer.hikari.HikariConfig
import doobie.syntax._
import doobie.implicits._

import doobie.hikari.HikariTransactor
import doobie.implicits.toSqlInterpolator
import doobie.util.transactor.Transactor
import cats.effect.Resource
import scala.concurrent.ExecutionContext
import cats.syntax.all._

case class DatabaseComponent[F[_]](transactor: Transactor[F])

object DatabaseComponent {

  def build[F[_]: Async: ContextShift](dbConfig: DBConfig): Resource[F, DatabaseComponent[F]] = {
    def createTransactor(config: DBConfig): Resource[F, HikariTransactor[F]] = {
      val poolConfig = new HikariConfig()
      poolConfig.setJdbcUrl(config.url)
//      poolConfig.setUsername(config.user)
//      poolConfig.setPassword(config.password)
//      poolConfig.setMaximumPoolSize(config.connectPoolSize)
      poolConfig.setPoolName("HIKARI-")

      val ce = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))
      val be =
        Blocker.liftExecutionContext(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4)))
      for {
        xa <- HikariTransactor.fromHikariConfig[F](poolConfig, ce, be)
      } yield xa
    }

    val createUsers = sql"""
      CREATE TABLE IF NOT EXISTS users (
        id   bigint auto_increment primary key,
        login VARCHAR NOT NULL UNIQUE,
        password  VARCHAR NOT NULL
      )
    """.update

    val createPurchases = sql"""
      CREATE TABLE IF NOT EXISTS purchases (
        id   bigint auto_increment primary key,
        amount DECIMAL NOT NULL,
        currency VARCHAR NOT NULL,
        comment VARCHAR,
        category VARCHAR NOT NULL
      )
    """.update


    def createTables(transactor: Transactor[F]): F[Unit] = {
      for {
        _ <- createUsers.run.transact(transactor)
        _ <- createPurchases.run.transact(transactor)
      } yield ()
    }

    for {
      main <- createTransactor(dbConfig)
      _    <- Resource.liftF(createTables(main))
    } yield DatabaseComponent(main)
  }

}
