package utilities

import com.twitter.util.{Return, Throw, Future ⇒ TFuture}

import scala.concurrent.{Future, Promise ⇒ ScalaPromise}

object FutureConverters {

  implicit class TwitterFutureToScalaFutureConverter[A](val twitterFuture: TFuture[A]) extends AnyVal {

    def asScala: Future[A] = {

      val scalaPromise = ScalaPromise[A]()
      twitterFuture.respond {
        case Return(value)    ⇒ scalaPromise.success(value)
        case Throw(exception) ⇒ scalaPromise.failure(exception)
      }
      scalaPromise.future
    }
  }
}
