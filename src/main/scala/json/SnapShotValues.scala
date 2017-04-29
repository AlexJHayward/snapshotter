package json

import play.api.libs.json._

case class SnapShotValues(rows: JsArray,
                          tags: JsArray,
                          templateList: JsArray,
                          time: JsObject,
                          title: JsString,
                          version: JsNumber)
