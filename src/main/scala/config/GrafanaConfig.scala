package config

import com.typesafe.config.{Config ⇒ TSConfig, ConfigFactory}

class GrafanaConfig {

  val config: TSConfig = ConfigFactory.load()

  val grafanaHost: String          = config.getString("grafana.instance.host")
  val grafanaPort: Int             = config.getInt("grafana.instance.port")
  val grafanaApiKey: String        = config.getString("grafana.apikey")
  val grafanaDashboardName: String = config.getString("grafana.dashboardName")

  val grafanaHeaders: Map[String, String] = Map(
    "Accept"        → "application/json",
    "Content-Type"  → "application/json",
    "Authorization" → s"Bearer $grafanaApiKey"
  )
}
