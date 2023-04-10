package io.ct2.cdkhelper

import software.constructs.Construct

def synthesize(builder: Construct ?=> Unit): Unit =
  val app = software.amazon.awscdk.App()
  builder(using app)
  app.synth()
