package json

import play.api.libs.json._

trait Templates {

  //todo this is shit

  private var rows: JsObject    = Json.obj()
  private var tags: JsArray     = Json.arr()
  private var version: JsNumber = JsNumber(0)

  def snapshotJson(rowsResult: JsResult[JsObject],
                   tagsResult: JsResult[JsArray],
                   versionResult: JsResult[JsNumber]): String = {

    if (rowsResult.isSuccess && tagsResult.isSuccess && versionResult.isSuccess) {
      rows = rowsResult.get
      tags = tagsResult.get
      version = versionResult.get
    }

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
          "rows" → Json.arr(
            rows
          ),
          "style" → "dark",
          "tags"  → tags,
          "templating" → Json.obj(
            "list" → Json.arr()
          ),
          "time"     → Json.obj(),
          "timezone" → "browser",
          "title"    → "Home",
          "version"  → version
        ),
        "expires" → 3600
      )
    )
  }
}
