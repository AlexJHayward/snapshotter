import com.twitter.finagle.Http
import com.twitter.finagle.http._
import com.twitter.io.Buf
import futureconvertors.FutureConverters._
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object Main extends App with MainConfig {

  val service = Http.client.withTls(host).newService(s"$host:$port")

  //todo janky formatting
  val eventualPost: Future[JsValue] = for {
    eventualDashboard ← getDashboard("some-fake-dashboard")
    dashboardJson = Json.parse(eventualDashboard.getContentString())
    eventualResponse ← createAndSendSnapshotJson(dashboardJson)
    json = Json.parse(eventualResponse.getContentString())
  } yield json

  eventualPost onComplete {
    case Success(rjson) ⇒ println(s"success $rjson")
    case Failure(ex)    ⇒ println(s"failure $ex")
  }

  Await.result(eventualPost, Duration.Inf)

//    .onComplete {
//      case Success(rjson) ⇒ sendSuccessMail(s"http://$host/${(rjson \ "url").get}")
//      case Failure(ex)    ⇒ sendFailureMail(ex)
//    }

  def createAndSendSnapshotJson(dashboard: JsValue): Future[Response] = {

    val snapshotJson: String = Json.stringify(
      Json.obj(
        "dashboard" → Json.obj(
          "editable"     → false,
          "hideControls" → true,
          "nav" → Json.arr(
            Json.obj(
              "enable" → false,
              "type"   → "timepicker"
            )
          ),
          "rows" → Json.arr(
            (dashboard \ "dashboard" \ "rows").getOrElse(Json.obj())
          ),
          "style" → "dark",
          "tags"  → (dashboard \ "dashboard" \ "tags").getOrElse(Json.arr()),
          "templating" → Json.obj(
            "list" → Json.arr()
          ),
          "time"     → Json.obj(),
          "timezone" → "browser",
          "title"    → "Home",
          "version"  → (dashboard \ "dashboard" \ "version").getOrElse(Json.toJson(0))
        ),
        "expires" → 3600
      )
    )

    val buffer = Buf.ByteArray.Owned(snapshotJson.getBytes())

    val createSnapshotRequest = RequestBuilder()
      .url(s"http://$host:$port/api/snapshots/")
      .addHeaders(headers)
      .build(Method.Post, Some(buffer))

    service(createSnapshotRequest).asScala
  }

  def getDashboard(dashboard: String): Future[Response] = {

    val request = RequestBuilder()
      .url(s"https://$host/api/dashboards/db/$dashboard")
      .addHeaders(headers)
      .build(Method.Get, None)

    service(request).asScala
  }

  def sendFailureMail(ex: Throwable): Future[Response] = ???
  def sendSuccessMail(url: String): Future[Response]   = ???
}
