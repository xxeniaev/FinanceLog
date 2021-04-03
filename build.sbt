name := "finance-log"

version := "0.1"

scalaVersion := "2.12.13"

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core"     % doobieVersion,
  "org.tpolecat" %% "doobie-h2" % doobieVersion,
  "org.typelevel" %% "cats-core" % "2.3.0",
  "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.17.19",
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "0.17.19",
  "com.softwaremill.sttp.client3" %% "akka-http-backend" % "3.2.0",
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-fs2" % "3.2.0",
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % "3.2.0",
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % "3.2.0",
)

libraryDependencies ++= Seq(

  // Start with this one
  "org.tpolecat" %% "doobie-core"      % "0.12.1",

  // And add any of these as needed
  "org.tpolecat" %% "doobie-h2"        % "0.12.1",          // H2 driver 1.4.200 + type mappings.
  "org.tpolecat" %% "doobie-hikari"    % "0.12.1",          // HikariCP transactor.
  "org.tpolecat" %% "doobie-specs2"    % "0.12.1" % "test", // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % "0.12.1" % "test"  // ScalaTest support for typechecking statements.


)
