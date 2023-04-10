package io.ct2.cdkhelper.ec2

import io.ct2.cdkhelper.buildArn
import io.ct2.cdkhelper.iam
import software.amazon.awscdk.ArnFormat
import software.amazon.awscdk.Resource
import software.amazon.awscdk.Stack
import software.amazon.awscdk.services.ec2.IInstance
import software.amazon.awscdk.services.ec2.InstanceProps
import software.amazon.awscdk.services.iam.Grant
import software.amazon.awscdk.services.iam.IGrantable
import software.constructs.Construct

sealed abstract class ImportedInstance private (scope: Construct, id: String) extends Resource(scope, id):
  def arn: String

  def grantSendSSMCommand(grantee: IGrantable): Grant =
    iam.grant(grantee = grantee, actions = Seq("ssm:SendCommand"), resourceArns = Seq(this.arn), scope = this)
object ImportedInstance:
  def fromInstanceId(id: String, instanceId: String)(using scope: Construct): ImportedInstance =
    new ImportedInstance(scope, id):
      given Stack = Stack of this

      override lazy val arn = buildArn(
        service = "ec2",
        resource = "instance",
        resourceName = Some(instanceId),
        arnFormat = Some(ArnFormat.SLASH_RESOURCE_NAME),
      )
