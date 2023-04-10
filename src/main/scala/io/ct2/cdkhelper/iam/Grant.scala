package io.ct2.cdkhelper.iam

import scala.jdk.CollectionConverters.given
import software.amazon.awscdk.services.iam.Grant
import software.amazon.awscdk.services.iam.GrantOnPrincipalOptions
import software.amazon.awscdk.services.iam.IGrantable
import software.constructs.Construct

def grant(
    grantee: IGrantable,
    actions: Seq[String] = Seq(),
    resourceArns: Seq[String] = Seq(),
    scope: Construct,
): Grant = Grant addToPrincipal GrantOnPrincipalOptions
  .builder()
  .grantee(grantee)
  .actions(actions.asJava)
  .resourceArns(resourceArns.asJava)
  .scope(scope)
  .build()
