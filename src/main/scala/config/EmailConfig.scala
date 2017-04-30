package config

import com.typesafe.config.{ConfigFactory, Config ⇒ TSConfig}

import scala.io.Source

class EmailConfig {

  val config: TSConfig = ConfigFactory.load()

  val emailHostName: String         = config.getString("email.hostName")
  val emailSmtpPort: Int            = config.getInt("email.smtpPort")
  val emailUsername: String         = config.getString("email.authentication.username")
  val emailPassword: String         = config.getString("email.authentication.password")
  val emailStartTLSEnabled: Boolean = config.getBoolean("email.startTLSEnabled")
  val emailSenderAddress: String    = config.getString("email.senderDetails.email")
  val emailSenderName: String       = config.getString("email.senderDetails.name")

  //todo probably not the greatest code ever written
  //the first in the list is the primary, all others are CC'd
  val emailReceiverAdresses: Seq[String] =
    Source
      .fromFile("src/main/resources/receiver_emails.txt")
      .mkString
      .split("\n")
      .toSeq
}
