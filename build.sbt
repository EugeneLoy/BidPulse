organization  := "org.bidpulse"

version       := "0.1"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

resolvers += "Spray" at "http://repo.spray.io"

libraryDependencies ++= {
  val akkaV = "2.3.5"
  val sprayV = "1.3.1"
  Seq(
    "io.spray"            %%  "spray-can"                     % sprayV,
    "io.spray"            %%  "spray-routing"                 % sprayV,
    "com.wandoulabs.akka" %%  "spray-websocket"               % "0.1.3",
    "com.typesafe.akka"   %%  "akka-actor"                    % akkaV,
    "com.typesafe.akka"   %%  "akka-persistence-experimental" % akkaV,
    "joda-time"           %   "joda-time"                     % "2.5",
    "org.joda"            %   "joda-convert"                  % "1.7",
    "io.spray"            %%  "spray-testkit"                 % sprayV   % "test",
    "com.typesafe.akka"   %%  "akka-testkit"                  % akkaV    % "test",
    "org.scalatest"       %%  "scalatest"                     % "2.2.2"  % "test"
  )
}

scoverage.ScoverageSbtPlugin.instrumentSettings

org.scoverage.coveralls.CoverallsPlugin.coverallsSettings

ScoverageKeys.minimumCoverage := 80

ScoverageKeys.failOnMinimumCoverage := true

ScoverageKeys.excludedPackages in ScoverageCompile := List(
  "org\\.bidpulse\\.example\\..*",
  "org\\.bidpulse\\.server\\..*" // TODO it might be worth to cover some potion of this package
).mkString(";")

Revolver.settings

Revolver.enableDebugging(port = 5005, suspend = false)
