package ru.dreamteam.infrastructure

import io.estatico.newtype.Coercible
import shapeless.LowPriority
import tethys.{JsonReader, JsonWriter}

object newtype {
    implicit def NewTypeEncoder[R, N](
                                       implicit
                                       LP: LowPriority,
                                       CC: Coercible[JsonReader[R], JsonReader[N]],
                                       R: JsonReader[R]
                                     ): JsonReader[N] = CC(R)

    implicit def NewTypeDecoder[R, N](
                                       implicit
                                       LP: LowPriority,
                                       CC: Coercible[JsonWriter[R], JsonWriter[N]],
                                       R: JsonWriter[R]
                                     ): JsonWriter[N] = CC(R)
}
