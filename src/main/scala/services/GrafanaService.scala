package services

import com.twitter.finagle.Http
import com.twitter.finagle.http.{Method, RequestBuilder, Response}
import com.twitter.io.Buf
import config.Config
import json.Templates
import play.api.libs.json.{JsValue, Json}
import utilities.FutureConverters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GrafanaService(config: Config) extends Templates {

  private val host         = config.host
  private val port         = config.port
  private val headers      = config.headers
  private val dashboardUrl = s"https://$host/api/dashboards/db"
  private val snapshotUrl  = s"http://$host:$port/api/snapshots/"

  private val service = Http.client
    .withTls(host)
    .newService(s"$host:$port")

  //todo janky formatting 🙃
  def createSnapshotForDashboard(dashboardName: String): Future[JsValue] =
    for {
      eventualDashboard ← getDashboard(dashboardName)
      dashboardJson = Json.parse(eventualDashboard.getContentString())
      eventualResponse ← createAndSendSnapshotJson(dashboardJson)
      json = Json.parse(eventualResponse.getContentString())
    } yield json

  def createAndSendSnapshotJson(dashboard: JsValue): Future[Response] = {

    val snapshot: String = snapshotJson(dashboard)

    val buffer = Buf.ByteArray.Owned(snapshot.getBytes())

    val request = RequestBuilder()
      .url(snapshotUrl)
      .addHeaders(headers)
      .build(Method.Post, Some(buffer))

    service(request).asScala
  }

  def getDashboard(dashboard: String): Future[Response] = {

    val request = RequestBuilder()
      .url(s"$dashboardUrl/$dashboard")
      .addHeaders(headers)
      .build(Method.Get, None)

    service(request).asScala
  }
}
