import config.Config
import services.GrafanaService
import services.email.EmailService

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {

  val config = new Config
  val grafanaService = new GrafanaService(config)
  val emailService = new EmailService(config)

  val eventualSnapshot = grafanaService.createSnapshotForDashboard("some-fake-dashboard")

  eventualSnapshot onComplete {
    case Success(rjson) ⇒ println(s"success $rjson")
    case Failure(ex)    ⇒ println(s"failure $ex")
  }

  //todo would be grand not to have to do this, maybe inf is too long? ⌛
  Await.result(eventualSnapshot, Duration.Inf)
  Thread.sleep(9999L)
}