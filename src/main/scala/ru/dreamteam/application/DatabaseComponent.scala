package ru.dreamteam.application

import java.util.Properties
import java.util.concurrent.Executors
import cats.effect.{Async, Blocker, ContextShift, Resource}
import com.zaxxer.hikari.HikariConfig
import doobie.free.connection.ConnectionIO
import doobie.hikari.HikariTransactor
import doobie.implicits.toSqlInterpolator
import doobie.util.transactor.Transactor
import ru.dreamteam.examples.Doobie.transactor

import scala.concurrent.ExecutionContext

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
        id   VARCHAR,
        login VARCHAR NOT NULL UNIQUE,
        password  VARCHAR NOT NULL
      )
    """.update

    val createPurchases = sql"""
      CREATE TABLE IF NOT EXISTS purchases (
        id   VARCHAR,
        amount DECIMAL,
        currency VARCHAR,
        comment VARCHAR,
        category VARCHAR 
      )
    """.update

    // это что и зачем
    for {
      main <- createTransactor(dbConfig)
    } yield DatabaseComponent(main)

//    def createTables(transactor: Transactor[F]): F[Unit] = {
//      val createConnection: ConnectionIO[Unit] = for {
//        _ <- createUsers.run
//        _ <- createPurchases.run
//      } yield ()
////      createConnection.transact(transactor).unsafeRunSync()
//      ???
//    }
//    ???
  }

}
