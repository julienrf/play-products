name := "play-products"

organization := "org.julienrf"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-functional" % "2.4.6",
  "com.chuusai" %% "shapeless" % "2.3.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % Test,
  "com.typesafe.play" %% "play-json" % "2.4.6" % Test
)

scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Yinline-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused-import",
  "-Ywarn-value-discard",
  "-Xlint",
  "-Xfuture"
)
