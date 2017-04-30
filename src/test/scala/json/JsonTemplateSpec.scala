package json

import fixture.PlayJsonFixture
import org.scalatest.{FunSuite, Matchers}

class JsonTemplateSpec
    extends FunSuite
    with Matchers
    with Templates
    with PlayJsonFixture {

  test("should create snapshot json string from dashboard json") {

    val snapshot = snapshotJson(actualDashboardJson)

    snapshot shouldBe completeSnapshotString
  }

  for ((title, json) <- dashboardJsonSeq)
  test(s"should create blank snapshot when $title element is missing from dashboard json") {

    val snapshot = snapshotJson(json)

    snapshot shouldBe blankSnapShotString
  }
}
