package ru.dreamteam.examples

import sttp.client._
import cats.syntax.all._
import cats.instances.all._
import com.twitter.util.Future
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.model.Uri
import zio.{Task, ZIO}
import zio.interop.twitter._
import zio.interop.catz._

object Sttp extends App {

  println(Uri("https://www.cbr-xml-daily.ru/daily_json.js"))

  val request = basicRequest
    .response(asStringAlways)
    .get(uri"https://www.cbr-xml-daily.ru/daily_json.js")

println(request.toCurl)
  val backend = AsyncHttpClientCatsBackend[Task]().flatMap {
    implicit backend =>
    request
      .send()

  }

  println(zio.Runtime.default.unsafeRun(backend))

}
