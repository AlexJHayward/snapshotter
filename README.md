# snapshotter

Snapshot grafana dashboards programmatically and email the results to someone.

Written in scala with play-json, finagle and hopefully something that can send emails.

## application.conf

Add the following to your `application.conf` file in `src/main/resources`, it's being manually added to the classpath by SBT right now because Typesafe config can't see it.

```apacheconf
grafana {

  apikey = "YOUR_API_KEY"

  instance {
    host = "YOUR_GRAFANA_INSTANCE"
    port = 443 #this needs to be https port or grafana will disagree with you
  }
}
```
