package json

import java.time.LocalDate

import play.api.libs.json._

trait Templates extends ImplicitJsonReads {

  def snapshotJson(dashboard: JsValue): String = {

    val valuesFromDashboard: JsResult[SnapShotValues] = dashboard.validate[SnapShotValues]

    //todo currently if any value fails (this is if the whole value is absent from the response, not if it is empty) then everything gets defaulted, this should be changed so that only the failed value gets defaulted

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

    val snapShotName = s"${values.title} ${LocalDate.now().toString}"

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
        "expires" → 0, //todo this represents "never" perhaps it should be adjusted
        "name" -> snapShotName
      )
    )
  }
}