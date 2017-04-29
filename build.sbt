name := "snapshotter"

version := "1.0"

scalaVersion := "2.12.2"

unmanagedResourceDirectories in Compile := Seq(baseDirectory.value / "src" / "main" / "resources")

libraryDependencies ++= Seq(
  "com.twitter"       %% "finagle-http" % "6.44.0",
  "com.typesafe.play" %% "play-json"    % "2.6.0-M1",
  "com.typesafe"      % "config"        % "1.2.1",
  "org.scala-lang"    % "scala-library" % scalaVersion.value
)
