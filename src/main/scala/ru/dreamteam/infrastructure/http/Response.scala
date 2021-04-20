package ru.dreamteam.infrastructure.http

import tethys.JsonWriter
import tethys._
import tethys.derivation.semiauto.{jsonReader, jsonWriter}

case class Response[T](payload: Option[T], trackingId: String, status: Status, error: Option[ErrorPayload])
object Response {
  implicit def responseWriter[T: JsonWriter]: JsonObjectWriter[Response[T]] = jsonWriter[Response[T]]
  implicit def responseReader[T: JsonReader]: JsonReader[Response[T]] = jsonReader[Response[T]]
}

case class ErrorPayload(message: String)
object ErrorPayload {
  implicit def errorPayloadWriter: JsonObjectWriter[ErrorPayload] = jsonWriter[ErrorPayload]
  implicit def errorPayloadReader: JsonReader[ErrorPayload] = jsonReader[ErrorPayload]
}