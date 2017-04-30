package services.email

import java.time.LocalDate

import config.EmailConfig
import play.api.libs.json._

class EmailService(config: EmailConfig) {

  private val date = LocalDate.now().toString

  //todo probably shouldn't be using a throwable here
  def sendMail(dashboardName: String, body: JsValue): Unit =
    send a createSuccessfulMail(dashboardName, body.getOrElse(Json.obj("sorry I was empty" → ":(")))

  def sendMail(dashboardName: String, exception: Throwable): Unit =
    send a createFailureMail(
      dashboardName,
      exception.getOrElse(new Throwable("The creation of the snapshot failed but it seems unclear why"))
    )

  def createSuccessfulMail(dashboardName: String, json: JsValue): Mail = {

    val message =
      s"""
         |The snapshot for dashboard $dashboardName has been created, it can be seen at aUrl .
         |
         |Cheers,
         |SnapshotBot
       """.stripMargin

    Mail(
      cc = createCCEmails,
      subject = s"Grafana Snapshot Created: $date",
      message = message,
      config = config
    )
  }

  def createFailureMail(dashboardName: String, ex: Throwable): Mail = {

    val message =
      s"""
         |The snapshot for dashboard $dashboardName was not created. :'(
         |
         |Please see the exception I saw below:
         |
         |Message: ${ex.getMessage}
         |Cause: ${ex.getCause}
         |
         |Soz,
         |SnapshotBot
       """.stripMargin

    Mail(
      cc = createCCEmails,
      subject = s"I failed to create a snapshot for $dashboardName",
      message = message,
      config = config
    )
  }

  private def createCCEmails: Seq[String] =
    if (config.emailReceiverAdresses.length > 1)
      config.emailReceiverAdresses.tail
    else
      Seq.empty
}
