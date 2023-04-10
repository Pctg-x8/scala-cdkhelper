package io.ct2.cdkhelper.ssm

import io.ct2.cdkhelper.buildArn
import io.ct2.cdkhelper.iam
import software.amazon.awscdk.Stack
import software.amazon.awscdk.services.iam.Grant
import software.amazon.awscdk.services.iam.IGrantable
import software.constructs.Construct

def allowGetAnyCommandInvocation(grantee: IGrantable)(using stack: Stack, scope: Construct): Grant = iam.grant(
  grantee = grantee,
  actions = Seq("ssm:GetCommandInvocation"),
  resourceArns = Seq(buildArn(service = "ssm", resource = "*")),
  scope = scope,
)
