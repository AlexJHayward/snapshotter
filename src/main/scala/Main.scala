import com.twitter.finagle.Http
import com.twitter.finagle.http.{Method, RequestBuilder, Response}
import com.twitter.io.Buf
import play.api.libs.json.{JsValue, Json}
import futureconvertors.FutureConverters._

import scala.concurrent.Future
import scala.util.{Failure, Success}

class Main extends App {

  private val host = s"dashboard.prod.hcom"
  private val port = 80

  private val client = Http.newService(s"$host:$port")

  val eventualDashboard = getDashboard("")

  val dashboardjson = eventualDashboard.onComplete {
    case Success(res) => Json.parse(res.content.toString) //todo what does this actually do to the buffer?
    case Failure(ex) => sendMail("nope")
  }

  val buffer =
    Buf.ByteArray.Owned(createSnapshotJson(dashboardjson).getBytes())

  val createSnapShotRequest = RequestBuilder()
    .url(s"http://$host:$port/api/snapshots/")
    .build(Method.Post, Some(buffer))

  val snapshotResponse = client(createSnapShotRequest).asScala

  def createSnapshotJson(dashboard: JsValue): String = {}

  def getDashboard(dashboard: String): Future[Response] = {

    val getDashboardRequest = RequestBuilder()
      .url(s"http://$host:$port/api/dashboards/db/$dashboard")
      .buildGet()

    client(getDashboardRequest).asScala
  }

  def sendMail(msg: String): Future[Unit] = ???
}
