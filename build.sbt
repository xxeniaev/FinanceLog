name := "finance-log"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  // for config reading
  "com.github.pureconfig" %% "pureconfig" % "0.14.1",

  // main effect
  "dev.zio" %% "zio"                 % "1.0.5",
  "dev.zio" %% "zio-interop-cats"    % "2.4.0.0",

  // default lib for happy being
  "org.typelevel" %% "cats-core" % "2.4.2",
  "org.typelevel" %% "cats-tagless-macros" % "0.13.0",

  // for http client
  "com.softwaremill.sttp.client" %% "core"            % "2.2.8",
  "com.softwaremill.sttp.client" %% "async-http-client-backend-cats" % "2.2.8",

  //  for http server
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-json-tethys" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-newtype" % "0.17.19",

  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"        % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml"  % "0.17.19",

  // json
  "com.tethys-json" %% "tethys-core" % "0.23.0",
  "com.tethys-json" %% "tethys-jackson" % "0.23.0",
  "com.tethys-json" %% "tethys-derivation" % "0.23.0",
  "com.tethys-json" %% "tethys-enumeratum" % "0.23.0",

  // newtype
  "io.estatico" %% "newtype" % "0.4.4",
  // enumerantum
  "com.beachape" %% "enumeratum" % "1.6.1",

  "tf.tofu" %% "derevo-tethys" % "0.12.3",


  "org.slf4j" % "slf4j-api" % "1.7.28",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

  // for DB
  "org.tpolecat" %% "doobie-core"      % "0.12.1",
  "org.tpolecat" %% "doobie-h2"        % "0.12.1",          // H2 driver 1.4.200 + type mappings.
  "org.tpolecat" %% "doobie-hikari"    % "0.12.1",          // HikariCP transactor.
  "org.tpolecat" %% "doobie-specs2"    % "0.12.1" % "test", // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % "0.12.1" % "test"  // ScalaTest support for typechecking statements.

)

scalacOptions := Seq(
  "-Ymacro-annotations"
)
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.3" cross CrossVersion.full)
