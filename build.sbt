val scala3Version = "3.2.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-cdkhelper",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    githubOwner := "Pctg-x8",
    githubRepository := "scala-cdkhelper",
    libraryDependencies ++= Seq(
      "software.amazon.awscdk" % "aws-cdk-lib" % "2.72.1"
    ),
  )
