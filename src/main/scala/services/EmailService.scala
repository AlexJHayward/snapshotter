package services

import com.twitter.finagle.http.Response
import config.Config

import scala.concurrent.Future

class EmailService(config: Config) {

  def sendFailureMail(ex: Throwable): Future[Response] = ???
  def sendSuccessMail(url: String): Future[Response]   = ???
}