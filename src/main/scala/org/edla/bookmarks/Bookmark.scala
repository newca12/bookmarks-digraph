package org.edla.bookmarks

import java.net.URL

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, _}
import akka.stream.ActorMaterializer
import org.edla.bookmarks.FirefoxJsonProtocol.Node

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

case class Bookmark(location: List[String], url: URL)

object Bookmark {

  val bookmarks: ListBuffer[Bookmark] = ListBuffer[Bookmark]()
  val location: ListBuffer[String]    = ListBuffer[String]()

  def readTree(node: Node): List[Bookmark] = {
    node.`type` match {
      case "text/x-moz-place-container" =>
        location += node.title
        if (node.children.isDefined) {
          node.children.get.foreach(readTree)
          location.trimEnd(1)
        } else {
          location.trimEnd(1)
        }
      case "text/x-moz-place-separator" =>
      case "text/x-moz-place" =>
        if (node.uri.get.startsWith("http") || node.uri.get.startsWith("file"))
          bookmarks += Bookmark(location.toList, new URL(node.uri.get))
    }
    bookmarks.toList
  }

  implicit val system: ActorSystem             = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def check(url: String): Try[StatusCode] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = url))

    Try {
      val response: HttpResponse = Await.result(responseFuture, 5.second)
      response match {
        case HttpResponse(_, headers, _, _) =>
          //println(headers)
          val statusCode = response.status
          response.discardEntityBytes()
          if (!url.startsWith("https") && (statusCode.intValue() == 301 || statusCode.intValue() == 302)) {
            return check(url.replaceFirst("http", "https"))
          }
          statusCode
      }
    }

  }

  def check(bookmark: Seq[Bookmark]): Unit = {
    val total = bookmarks.size
    var n     = 0
    for (bookmark <- bookmarks) {
      n = n + 1
      val url = bookmark.url.toString
      if (url.startsWith("http") || url.startsWith("ws")) {
        val r: Try[StatusCode] = Bookmark.check(bookmark.url.toString)

        r match {
          case Success(statusCode) =>
            if (!List(200, 301, 302).contains(statusCode.intValue())) println(s"$n/$total $statusCode $bookmark")
          case Failure(ex) => println(s"$n/$total $bookmark ${ex.getMessage}")
        }
      } else println(s"$n/$total $bookmark")
    }
  }

}
