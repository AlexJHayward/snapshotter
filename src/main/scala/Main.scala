import com.twitter.finagle.Http
import com.twitter.finagle.http.{Method, RequestBuilder, Response}
import com.twitter.io.Buf
import futureconvertors.FutureConverters._
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class Main extends App {

  private val host = s"dashboard.prod.hcom"
  private val port = 80

  private val client = Http.newService(s"$host:$port")

  val eventualResponse = for {
    eventualDashboard <- getDashboard("")
    dashboardJson     = Json.parse(eventualDashboard.content.toString)
    eventualResponse  <- createAndSendSnapshotJson(dashboardJson)
    json              = Json.parse(eventualResponse.content.toString)
  } yield json

  eventualResponse.onComplete {
    case Success(rjson) =>
      sendUrlsMail("http://dashboard.prod.hcom/" + (rjson \ "url").get)
    case Failure(ex) => sendFailureMail(ex)
  }

  def createAndSendSnapshotJson(dashboard: JsValue): Future[Response] = {

    val snapshotJson = Json.obj(
      "dashboard" -> Json.obj(
        "editable" -> false,
        "hideControls" -> true,
    "nav":[
    {
      "enable":false,
      "type":"timepicker"
    }
    ],
    "rows": [
    {

    }
    ],
    "style":"dark",
    "tags":[],
    "templating":{
      "list":[
      ]
    },
    "time":{
    },
    "timezone":"browser",
    "title":"Home",
    "version":5
  },
  "expires": 3600
      )
    )

    val buffer = Buf.ByteArray.Owned(snapshotJson.getBytes())

    val createSnapShotRequest = RequestBuilder()
      .url(s"http://$host:$port/api/snapshots/")
      .build(Method.Post, Some(buffer))

    client(createSnapShotRequest).asScala
  }

  def getDashboard(dashboard: String): Future[Response] = {

    val getDashboardRequest = RequestBuilder()
      .url(s"http://$host:$port/api/dashboards/db/$dashboard")
      .buildGet()

    client(getDashboardRequest).asScala
  }

  def sendFailureMail(ex: Throwable): Future[Response] = ???
  def sendUrlsMail(url: String): Future[Response] = ???
}

case class SnapshotBody(rows:) {

  def toJsonString: String = ???
}