package edu.knoldus

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import edu.knoldus.WorkerActor._

object JsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val fileRequest: RootJsonFormat[FileRequest] = jsonFormat2(FileRequest)
  implicit val fileContentRequest: RootJsonFormat[FileContentRequest] = jsonFormat1(FileContentRequest)
  implicit val fileContentResponse: RootJsonFormat[FileContentResponse] = jsonFormat2(FileContentResponse)

  implicit val fileDeleteRequest: RootJsonFormat[FileDeleteRequest] = jsonFormat1(FileDeleteRequest)
  implicit val fileDeleteResponse: RootJsonFormat[FileResponse] = jsonFormat2(FileResponse)

  implicit val renameFilenameRequest: RootJsonFormat[RenameFilenameRequest] = jsonFormat2(RenameFilenameRequest)
  implicit val fibonacciResponse: RootJsonFormat[FibonacciResponse] = jsonFormat2(FibonacciResponse)

}
