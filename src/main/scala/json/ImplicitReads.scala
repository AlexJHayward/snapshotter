package json

import play.api.libs.json._
import play.api.libs.functional.syntax._

object ImplicitReads {

  implicit val dashboardReads: Reads[SnapShotValues] = (
    (JsPath \ "dashboard" \ "rows").read[JsArray] and
      (JsPath \ "dashboard" \ "tags").read[JsArray] and
      (JsPath \ "dashboard" \ "templating" \ "list").read[JsArray] and
      (JsPath \ "dashboard" \ "time").read[JsObject] and
      (JsPath \ "dashboard" \ "title").read[JsString] and
      (JsPath \ "meta" \ "version").read[JsNumber]
    )(SnapShotValues.apply _)
}
