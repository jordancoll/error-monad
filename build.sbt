val dottyVersion = "0.26.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "error-monad",
    version := "0.1.0",

    scalacOptions ++= Seq("-Xfatal-warnings"),

    scalaVersion := dottyVersion,

    libraryDependencies ++= Seq(
      "com.twitter" % "util-core_2.13" % "20.9.0"
    )
  )
