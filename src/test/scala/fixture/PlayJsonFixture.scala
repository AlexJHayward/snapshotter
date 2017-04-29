package fixture

import json.SnapShotValues
import play.api.libs.json._

import json.ImplicitReads._

import scala.io.Source

trait PlayJsonFixture {

  val valuesFromDashboardJson: SnapShotValues = getJsonFromDashboardFile.validate[SnapShotValues].get

  val completeSnapshotJson: JsValue = snapshotJsonWith(valuesFromDashboardJson)
  val completeDashboardJson: JsValue = getJsonFromDashboardFile

  val completeSnapshotString: String  = Json.stringify(completeSnapshotJson)
  val completeDashboardString: String = Json.stringify(completeDashboardJson)

  def snapshotJsonWith(values: SnapShotValues): JsValue = Json.obj(
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

  def getJsonFromDashboardFile: JsValue = {

    val filename = "src/test/resources/actualdashboard.json"
    val file     = Source.fromFile(filename).mkString

    Json.parse(file)
  }
}