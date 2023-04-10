val scala3Version = "3.2.2"

lazy val root = project
  .in(file("."))
  .settings(
    organization := "io.ct2",
    name := "cdkhelper",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    githubOwner := "Pctg-x8",
    githubRepository := "scala-cdkhelper",
    libraryDependencies ++= Seq(
      "software.amazon.awscdk" % "aws-cdk-lib" % "2.72.1"
    ),
  )
