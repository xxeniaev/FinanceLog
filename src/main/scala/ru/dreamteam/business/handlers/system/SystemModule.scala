package ru.dreamteam.business.handlers.system

import org.http4s.HttpRoutes
import ru.dreamteam.business.handlers.system.SystemModule.VersionInfo
import ru.dreamteam.infrastructure.http.{HttpModule, Response}
import ru.dreamteam.infrastructure.http
import sttp.tapir.{endpoint, Endpoint}
import sttp.tapir.generic.auto._
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonObjectWriter, JsonReader, JsonWriter}
import zio._

import cats.syntax.all._
import zio.interop.catz._
import zio.interop.catz.implicits._

class SystemModule(
  implicit
  runtime: zio.Runtime[Unit]
) extends HttpModule[Task] {

  val versionEndpoint = endpoint
    .get
    .in("version")
    .out(jsonBody[Response[VersionInfo]])
    .handle(_ => UIO(VersionInfo("test")))

  override def httpRoutes(
    implicit
    serverOptions: Http4sServerOptions[Task]
  ): HttpRoutes[Task] = Http4sServerInterpreter.toRoutes(versionEndpoint)

}

object SystemModule {

  case class VersionInfo(info: String)

  object VersionInfo {

    implicit val versionWriter: JsonObjectWriter[VersionInfo] = jsonWriter[VersionInfo]
    implicit val versionReader: JsonReader[VersionInfo]       = jsonReader[VersionInfo]
  }

}
