package ru.dreamteam.application

import java.util.Properties
import java.util.concurrent.Executors

import cats.effect.{Async, Blocker, ContextShift, Resource}
import com.zaxxer.hikari.HikariConfig
import doobie.hikari.HikariTransactor
import doobie.util.transactor.Transactor

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
      val be = Blocker.liftExecutionContext(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4)))
      for {
        xa <- HikariTransactor.fromHikariConfig[F](poolConfig, ce, be)
      } yield xa
    }

    for {
      main <- createTransactor(dbConfig)
    } yield DatabaseComponent(main)
  }
}
