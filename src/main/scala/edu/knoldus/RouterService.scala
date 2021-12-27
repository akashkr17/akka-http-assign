package edu.knoldus

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import edu.knoldus.JsonSupport._
import edu.knoldus.WorkerActor._
import scala.concurrent.Future
import scala.concurrent.duration._

trait RouterService extends Directives {
  implicit val system: ActorSystem
  import scala.language.postfixOps

  implicit val actionTimeOut: Timeout = {
    Timeout(40 seconds)
  }


  def routes(actor: ActorRef): Route = fileRoute(actor)

  import akka.pattern.ask

  def fileRoute(actor: ActorRef): Route = path("fibonacci" / Segment) { number =>
    get {
      val processFibonacci: Future[FibonacciResponse] = ask(actor ,GetNumber(number.toInt)).mapTo[FibonacciResponse]
      onSuccess(processFibonacci) {
        res =>
          complete(StatusCodes.OK,res)
      }
    }
  } ~
  pathPrefix("file") {
    (post & entity(as[FileRequest])) { fileRequest =>
      pathEnd {
        val processFile: Future[FileResponse] = ask(actor, fileRequest).mapTo[FileResponse]
        onSuccess(processFile) {
          res =>
            complete(StatusCodes.OK, res)
        }
      }
    } ~
      path("getFileContent") {
        (post & entity(as[FileContentRequest])) { fileContentRequest =>
          pathEnd {
            val processFileContent: Future[FileContentResponse] = ask(actor, fileContentRequest).mapTo[FileContentResponse]
            onSuccess(processFileContent) {
              res =>
                complete(StatusCodes.OK, res)
            }
          }
        }
      } ~
    path("rename") {
      (put & entity(as[RenameFilenameRequest])) { renameFilenameRequest =>
        pathEnd {
          val renameFilename: Future[FileResponse] = ask(actor, renameFilenameRequest).mapTo[FileResponse]
          onSuccess(renameFilename) {
            res =>
              complete(StatusCodes.OK, res)
          }
        }
      }
    }
  } ~
    path("file" / Segment) { filename =>
    delete {
      val deleteFile: Future[FileResponse] = ask(actor, FileDeleteRequest(filename) ).mapTo[FileResponse]
      onSuccess(deleteFile) {
        res =>
          complete(StatusCodes.OK, res)
      }
    }
  }
}

