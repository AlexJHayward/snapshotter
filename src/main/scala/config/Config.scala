package config

class Config extends GrafanaConfig {

  val headers: Map[String, String] = Map(
    "Accept"        → "application/json",
    "Content-Type"  → "application/json",
    "Authorization" → s"Bearer $apiKey"
  )
}