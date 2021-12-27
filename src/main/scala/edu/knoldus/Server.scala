package edu.knoldus

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}

object Server extends App with RouterService {



  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val config: Config = ConfigFactory.load("application.conf")
  val address = config.getString("app.host")
  val port = config.getInt("app.port")

  val newActor = system.actorOf(Props[WorkerActor],"newActor")

val route: Route = routes(newActor)
  val binding = Http().bindAndHandle(route, address,port)
  println(s"Server is listening on $address: $port ")

}
