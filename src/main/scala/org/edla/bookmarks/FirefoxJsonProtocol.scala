package org.edla.bookmarks
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object FirefoxJsonProtocol extends DefaultJsonProtocol {

  case class Node(
      guid: String,
      title: String,
      index: Int,
      dateAdded: Long,
      lastModified: Long,
      id: Int,
      typeCode: Int,
      `type`: String,
      root: Option[String],
      children: Option[List[Node]],
      uri: Option[String]
  )

  implicit val nodeFormat: RootJsonFormat[Node] = rootFormat(
    lazyFormat(
      jsonFormat(Node,
                 "guid",
                 "title",
                 "index",
                 "dateAdded",
                 "lastModified",
                 "id",
                 "typeCode",
                 "type",
                 "root",
                 "children",
                 "uri")))

}
