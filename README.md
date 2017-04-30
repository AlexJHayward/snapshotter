# snapshotter

Snapshot grafana dashboards programmatically and email the results to someone.

Meant to be run as a cron job or something, with the idea of using grafana snapshots to document monthly average stats, this is a fairly specific usecase but it was useful to me and a nice bit of finagle/play-json practice.

Written in scala with play-json, finagle and hopefully something that can send emails.

Shout out to [mariussoutier](https://github.com/mariussoutier) for the [scala email sending gist](https://gist.github.com/mariussoutier/3436111).

## application.conf

Add the following to your `application.conf` file in `src/main/resources`, it's being manually added to the classpath by SBT right now because Typesafe config can't see it.

This is also way too much configurable stuff, I feally need to do some sorting out here.

```apacheconf
grafana {

  dashboardName = "some-fake-dashboard" //this is the dashboard to be snapshotted, I haven't yet made it work with multiple dashboards

  apikey = "YOUR_API_KEY"

  instance {
    host = "YOUR_GRAFANA_INSTANCE"
    port = 443 #this needs to be https port or grafana will disagree with you
  }
}

email {

  hostName = "smtp.gmail.com" //This can be whatever mail provider you use, just gmail for example
  smtpPort = 587

  startTLSEnabled = true //should always be true I think

  authentication {
    username = "YOUR_EMAIL_LOGIN"
    password = "YOUR_EMAIL_PASS" //I'm going to make this a runtime argument soon
  }

  senderDetails {
    name = "snapshotter"
    email = "WHOEVER_SNAPSHOTTER_SHOULD_BE_SENDING_FROM"
  }
}
```

## Receiver emails
in `src/main/resources` add a file called receiver_emails.txt, this is read assuming that there is a valid email on each line with no delimiter character other than the newline `\n`, I haven't included any validation for this because I'm a lazy fuck. Also, the first email is the one used in the To field of the email, all others are CC'd in.