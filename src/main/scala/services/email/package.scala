package services

import config.EmailConfig

/**
  * taken from https://gist.github.com/mariussoutier/3436111 by mariussoutier
  *
  * I've removed the stuff that snapshotter doesn't need to know about
  */
package object email {

  implicit def stringToSeq(single: String): Seq[String] = Seq(single)
  implicit def liftToOption[T](t: T): Option[T]         = Some(t)

  sealed abstract class MailType
  case object Plain     extends MailType
  case object Rich      extends MailType
  case object MultiPart extends MailType

  case class Mail(cc: Seq[String] = Seq.empty,
                  bcc: Seq[String] = Seq.empty,
                  subject: String,
                  message: String,
                  config: EmailConfig)

  object send {

    def a(mail: Mail) {

      import org.apache.commons.mail._

      val commonsMail: Email = new SimpleEmail().setMsg(mail.message)

      commonsMail.setHostName(mail.config.emailHostName)
      commonsMail.setSmtpPort(mail.config.emailSmtpPort)
      commonsMail.setAuthentication(mail.config.emailUsername, mail.config.emailPassword)
      commonsMail.setStartTLSEnabled(mail.config.emailStartTLSEnabled)

      // Can't add these via fluent API because it produces exceptions
      commonsMail.addTo(mail.config.emailReceiverAdresses.head)
      mail.cc foreach commonsMail.addCc
      mail.bcc foreach commonsMail.addBcc

      commonsMail.setFrom(mail.config.emailSenderAddress, mail.config.emailSenderName).setSubject(mail.subject).send()
    }
  }
}
