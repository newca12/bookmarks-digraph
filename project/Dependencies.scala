import sbt._

object Version {
  val htmlCleaner = "2.22"
  val sprayJson   = "1.3.5"
}

object Dependencies {
  val htmlCleaner = "net.sourceforge.htmlcleaner" % "htmlcleaner" % Version.htmlCleaner
  val sprayJson   = "io.spray"                    %% "spray-json" % Version.sprayJson
}
