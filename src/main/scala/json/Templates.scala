package json

import play.api.libs.json._

import ImplicitReads._

trait Templates {

  def snapshotJson(dashboard: JsValue): String = {

    val valuesFromDashboard: JsResult[SnapShotValues] = dashboard.validate[SnapShotValues]

    val values = valuesFromDashboard.getOrElse(
      SnapShotValues(
        rows = Json.arr(),
        tags = Json.arr(),
        templateList = Json.arr(),
        time = Json.obj("from" → "now-24h", "to" → "now"),
        title = JsString("Title Not Given"),
        version = JsNumber(0)
      )
    )

    Json.stringify(
      Json.obj(
        "dashboard" → Json.obj(
          "editable"     → false,
          "hideControls" → true,
          "nav" → Json.arr(
            Json.obj(
              "enable" → false,
              "type"   → "timepicker"
            )
          ),
          "rows"  → values.rows,
          "style" → "dark",
          "tags"  → values.tags,
          "templating" → Json.obj(
            "list" → values.templateList
          ),
          "time"     → values.time,
          "timezone" → "browser",
          "title"    → values.title,
          "version"  → values.version
        ),
        "expires" → 3600
      )
    )
  }
}