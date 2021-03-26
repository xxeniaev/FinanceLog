name := "finance-log"

version := "0.1"

scalaVersion := "2.12.13"

libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-finatra-server-cats" % "0.17.19"

libraryDependencies ++= Seq(

  // Start with this one
  "org.tpolecat" %% "doobie-core"      % "0.12.1",

  // And add any of these as needed
  "org.tpolecat" %% "doobie-h2"        % "0.12.1",          // H2 driver 1.4.200 + type mappings.
  "org.tpolecat" %% "doobie-hikari"    % "0.12.1",          // HikariCP transactor.
  "org.tpolecat" %% "doobie-postgres"  % "0.12.1",          // Postgres driver 42.2.19 + type mappings.
  "org.tpolecat" %% "doobie-quill"     % "0.12.1",          // Support for Quill 3.6.1
  "org.tpolecat" %% "doobie-specs2"    % "0.12.1" % "test", // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % "0.12.1" % "test"  // ScalaTest support for typechecking statements.

)
