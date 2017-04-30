import config.{EmailConfig, GrafanaConfig}
import services.GrafanaService
import services.email.EmailService

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {

  val emailConfig   = new EmailConfig
  val grafanaConfig = new GrafanaConfig

  val grafanaService = new GrafanaService(grafanaConfig)
  val emailService   = new EmailService(emailConfig)

  val dashboardName = grafanaConfig.grafanaDashboardName

  val eventualSnapshot = grafanaService.createSnapshotForDashboard(dashboardName)

  eventualSnapshot onComplete {
    case Success(rjson) ⇒ emailService.sendMail(dashboardName, rjson)
    case Failure(ex)    ⇒ emailService.sendMail(dashboardName, ex)
  }

  //todo would be grand not to have to do this, maybe inf is too long? ⌛
  Await.result(eventualSnapshot, Duration.Inf)
  Thread.sleep(9999L)
}