# snapshotter

Snapshot grafana dashboards programmatically and email the results to someone.

Meant to be run as a cron job or something, with the idea of using grafana snapshots to document monthly average stats, this is a fairly specific usecase but it was useful to me and a nice bit of finagle/play-json practice.

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
