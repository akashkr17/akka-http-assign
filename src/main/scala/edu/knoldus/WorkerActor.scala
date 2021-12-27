package edu.knoldus

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor.Actor

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try, Using}


class WorkerActor extends Actor {
  import WorkerActor._

  override def receive: Receive = {
    case GetNumber(n) =>
      val res = getFibonacci(n)
      sender ! res

    case FileRequest(filename,fileContent) =>
      val res = createFile(filename,fileContent)
      sender ! res

    case FileContentRequest(filename) =>
      val res = getFileContent(filename)
      sender ! res

    case FileDeleteRequest(filename) =>
      val res = deleteFile(filename)
      sender ! res


    case RenameFilenameRequest(filename,newfilename) =>
      val res = renameFilename(filename,newfilename)
      sender ! res
  }

  def getFibonacci(n: Int): FibonacciResponse = {

    val result = fibonacci(n)
    result match {
      case data: BigInt =>   FibonacciResponse(n,data)
    }
  }
  def createFile(filename: String,fileContent: String): FileResponse = {
    val file = new File(filename)
    if (file.exists){
      FileResponse(status = false,s"File $filename already exits")
    } else {
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(fileContent)
      bw.close()
      FileResponse(status = true,s"New file $filename created ")
    }
  }
  def getFileContent(filename: String): FileContentResponse = {

    import scala.io.Source
    val file = new File(filename)
    if (file.exists){
      val fileContents: Try[String] = Using(Source.fromFile(filename)) { source => source.mkString }
      fileContents match {
        case Success(content) => FileContentResponse(filename,content)
        case Failure(e) => throw new IllegalStateException(e)
      }
    }
    else {
      FileContentResponse(filename,"Does Not Exist")
    }
  }

  def deleteFile(filename: String): FileResponse = {
    val file = new File(filename)
    if (file.exists) {
      file.delete()
      println(s"file $filename Delete")
      FileResponse(status = true,s"File $filename Deleted Successfully")

    }
    else {
      FileResponse(status = false,s"File $filename Not exists")
    }
  }

  def renameFilename(filename: String,newfilename: String): FileResponse = {

    val file = new File(filename)
    if (file.exists) {
      file.renameTo(new File(newfilename))
      FileResponse(status = true,s"File $filename rename to $newfilename Successfully")
    }
    else
      FileResponse(status = false,s"File $filename not exits")

  }



  def fibonacci(x: Int): BigInt = {
    @tailrec def fibHelper(x: Int, prev: BigInt = 0, next: BigInt = 1): BigInt = x match {
      case 0 => prev
      case 1 => next
      case _ => fibHelper(x - 1, next, next + prev)
    }
    fibHelper(x)
  }

}

object WorkerActor {

  case class GetNumber(n: Int)
  final case class FibonacciResponse(number: BigInt, result: BigInt)

  final case class FileRequest(filename: String, fileContent: String)
  final case class RenameFilenameRequest(filename: String, newfilename: String)
  final case class FileContentRequest(filename: String)
  final case class FileDeleteRequest(filename: String)


  final case class FileResponse(status: Boolean, description: String)
  final case class FileContentResponse(filename: String, fileContent: String)

}
