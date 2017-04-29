import com.typesafe.config.{Config, ConfigFactory}

trait MainConfig {

  val config: Config = ConfigFactory.load()

  val host: String   = config.getString("grafana.instance.host")
  val port: Int      = config.getInt("grafana.instance.port")
  val apiKey: String = config.getString("grafana.apikey")

  val headers: Map[String, String] = Map(
    "Accept"        → "application/json",
    "Content-Type"  → "application/json",
    "Authorization" → s"Bearer $apiKey"
  )
}