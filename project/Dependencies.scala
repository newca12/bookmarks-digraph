import sbt._

object Version {
  val htmlCleaner = "2.22"
  val sprayJson   = "1.3.5"
  val akkaHttp    = "10.1.7"
  val akka        = "2.5.19"
}

object Dependencies {
  val htmlCleaner = "net.sourceforge.htmlcleaner" % "htmlcleaner"  % Version.htmlCleaner
  val sprayJson   = "io.spray"                    %% "spray-json"  % Version.sprayJson
  val akkaHttp    = "com.typesafe.akka"           %% "akka-http"   % Version.akkaHttp
  val akkaStream  = "com.typesafe.akka"           %% "akka-stream" % Version.akka
}
