package json

import fixture.PlayJsonFixture
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FunSuite, Matchers}

class JsonTemplateSpec
    extends FunSuite
    with Matchers
    with Templates
    with PlayJsonFixture
    with TableDrivenPropertyChecks {

  test("should create snapshot json string from dashboard json") {

    val snapshot = snapshotJson(completeDashboardJson)

    snapshot shouldBe completeSnapshotString
  }
}
