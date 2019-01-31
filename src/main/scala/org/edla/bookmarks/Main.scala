package org.edla.bookmarks
import org.edla.bookmarks.FirefoxJsonProtocol.Node
import spray.json._

import scala.io.Source

object Main extends App {
  if (args.length == 0) {
    println("firefox JSON filename is needed")
    sys.exit(1)
  }
  val filename = args(0)
  //val filename                 = "/tmp/bookmarks-2019-01-31.json"
  val fileContents             = Source.fromFile(filename).getLines.mkString
  val node                     = fileContents.parseJson.convertTo[Node]
  val bookmarks: Seq[Bookmark] = Bookmark.readTree(node)
  Bookmark.check(bookmarks)
}
