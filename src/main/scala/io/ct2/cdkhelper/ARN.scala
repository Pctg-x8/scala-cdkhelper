package io.ct2.cdkhelper

import software.amazon.awscdk.ArnComponents
import software.amazon.awscdk.ArnFormat
import software.amazon.awscdk.Stack

def buildArn(
    service: String,
    region: Option[String] = None,
    resource: String,
    resourceName: Option[String] = None,
    arnFormat: Option[ArnFormat] = None,
)(using
    stack: Stack
): String =
  val components = ArnComponents.builder().service(service).resource(resource)
  for r <- region do components region r
  for r <- resourceName do components resourceName r
  for r <- arnFormat do components arnFormat r
  stack formatArn components.build()
