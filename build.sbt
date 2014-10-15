organization  := "org.bidpulse"

version       := "0.1"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.5"
  val sprayV = "1.3.1"
  Seq(
    "io.spray"            %%  "spray-can"       % sprayV,
    "io.spray"            %%  "spray-routing"   % sprayV,
    "io.spray"            %%  "spray-testkit"   % sprayV   % "test",
    "com.typesafe.akka"   %%  "akka-actor"      % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"    % akkaV    % "test",
    "org.specs2"          %%  "specs2-core"     % "2.3.11" % "test", // TODO get rid of specs2 in favor of scalatest
    "org.scalatest"       %%  "scalatest"       % "2.2.2"  % "test",
    "joda-time"           %   "joda-time"       % "2.5"
  )
}

scoverage.ScoverageSbtPlugin.instrumentSettings

org.scoverage.coveralls.CoverallsPlugin.coverallsSettings

ScoverageKeys.minimumCoverage := 60

ScoverageKeys.failOnMinimumCoverage := true

ScoverageKeys.excludedPackages in ScoverageCompile := List(
  "org\\.bidpulse\\.example\\..*",
  "org\\.bidpulse\\.Boot"
).mkString(";")

Revolver.settings

Revolver.enableDebugging(port = 5005, suspend = false)
