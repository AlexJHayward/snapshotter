import com.twitter.finagle.Http
import com.twitter.finagle.http._
import com.twitter.io.Buf
import config.Config
import json.Templates
import play.api.libs.json._
import utilities.FutureConverters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object Main extends App with Config with Templates {

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

    val snapshot: String = snapshotJson(
      (dashboard \ "dashboard" \ "rows").validate[JsObject],
      (dashboard \ "dashboard" \ "tags").validate[JsArray],
      (dashboard \ "dashboard" \ "version").validate[JsNumber]
    )

    val buffer = Buf.ByteArray.Owned(snapshot.getBytes())

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
