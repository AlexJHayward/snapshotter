package config

import com.typesafe.config.{Config ⇒ TSConfig, ConfigFactory}

trait GrafanaConfig {

  val config: TSConfig = ConfigFactory.load()

  val host: String   = config.getString("grafana.instance.host")
  val port: Int      = config.getInt("grafana.instance.port")
  val apiKey: String = config.getString("grafana.apikey")
}
