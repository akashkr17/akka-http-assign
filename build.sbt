name := "assign-akka-http"

version := "0.1"

scalaVersion := "2.13.7"


lazy val akkaVersion = "2.6.17"
val AkkaHttpVersion = "10.2.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
 "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion

)