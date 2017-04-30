package fixture

import java.time.LocalDate

import json.{ImplicitJsonReads, SnapShotValues}
import play.api.libs.json._

import scala.io.Source

trait PlayJsonFixture extends ImplicitJsonReads {

//  val (dashboardNoRowsJson, dashboardNoRowsValues)       = valuesFromDashboardJson("dashboard_no_rows")
//  val (dashboardNoTagsJson, dashboardNoTagsValues)       = valuesFromDashboardJson("dashboard_no_tags")
//  val (dashboardNoTimeJson, dashboardNoTimeValues)       = valuesFromDashboardJson("dashboard_no_time")
//  val (dashboardNoTitleJson, dashboardNoTitleValues)     = valuesFromDashboardJson("dashboard_no_title")
//  val (dashboardNoVersionJson, dashboardNoVersionValues) = valuesFromDashboardJson("dashboard_no_version")
//  val (dashboardNoTemplatingListJson, dashboardNoTemplatingListValues) = valuesFromDashboardJson(
//    "dashboard_no_templating_list"
//  )

  val dashboardJsonSeq: Seq[(String, JsValue)] = Seq(
    ("Rows", jsonFromFile("dashboard_no_rows")),
    ("Tags", jsonFromFile("dashboard_no_tags")),
    ("Time", jsonFromFile("dashboard_no_time")),
    ("Title", jsonFromFile("dashboard_no_title")),
    ("Version", jsonFromFile("dashboard_no_version")),
    ("Templating List", jsonFromFile("dashboard_no_templating_list"))
  )

  val actualDashboardJson: JsValue          = jsonFromFile("actual_dashboard")
  val actualDashboardValues: SnapShotValues = valuesFromFile("actual_dashboard")
  val completeSnapshotJson: JsValue         = snapshotJsonWith(actualDashboardValues, LocalDate.now().toString)

  val completeSnapshotString: String  = Json.stringify(completeSnapshotJson)
  val completeDashboardString: String = Json.stringify(actualDashboardJson)

  val blankSnapShotString: String = Json.stringify(
    snapshotJsonWith(
      SnapShotValues(
        rows = Json.arr(),
        tags = Json.arr(),
        templateList = Json.arr(),
        time = Json.obj("from" → "now-24h", "to" → "now"),
        title = JsString("Title Not Given"),
        version = JsNumber(0)
      ),
      LocalDate.now().toString
    )
  )

  def snapshotJsonWith(values: SnapShotValues, date: String): JsValue = Json.obj(
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
    "expires" → 0,
    "name"    → s"${values.title} $date"
  )

  def jsonFromFile(name: String): JsValue = getJsonFromDashboardFile(name)

  def valuesFromFile(name: String): SnapShotValues = jsonFromFile(name).validate[SnapShotValues].get

  private def getJsonFromDashboardFile(name: String): JsValue = {

    val filename = s"src/test/resources/$name.json"
    val file     = Source.fromFile(filename).mkString

    Json.parse(file)
  }
}