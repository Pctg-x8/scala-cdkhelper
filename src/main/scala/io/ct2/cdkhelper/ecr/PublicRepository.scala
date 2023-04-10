package io.ct2.cdkhelper.ecr

import io.ct2.cdkhelper.buildArn
import io.ct2.cdkhelper.iam
import software.amazon.awscdk.ArnFormat
import software.amazon.awscdk.ResourceProps
import software.amazon.awscdk.Stack
import software.amazon.awscdk.services.codecommit.RepositoryProps
import software.amazon.awscdk.services.ecr.RepositoryBase
import software.amazon.awscdk.services.iam.AddToResourcePolicyResult
import software.amazon.awscdk.services.iam.Grant
import software.amazon.awscdk.services.iam.IGrantable
import software.amazon.awscdk.services.iam.PolicyStatement
import software.constructs.Construct

private sealed abstract class PublicRepositoryBase(scope: Construct, id: String, props: ResourceProps = null)
    extends RepositoryBase(scope, id, props):
  def grantPush(grantee: IGrantable): Grant =
    val r = this.grant(
      grantee,
      "ecr-public:BatchCheckLayerAvailability",
      "ecr-public:PutImage",
      "ecr-public:InitiateLayerUpload",
      "ecr-public:UploadLayerPart",
      "ecr-public:CompleteLayerUpload",
    )
    iam.grant(
      grantee = grantee,
      actions = Seq("ecr-public:GetAuthorizationToken"),
      resourceArns = Seq("*"),
      scope = this,
    )
    r

final class PublicRepository(scope: Construct, id: String, props: RepositoryProps)
    extends PublicRepositoryBase(scope, id, ResourceProps.builder().physicalName(props.getRepositoryName()).build()):
  given Stack = Stack of this

  override def getRepositoryName() = props.getRepositoryName()
  override def getRepositoryArn() = buildArn(
    service = "ecr-public",
    region = Some(""),
    resource = "repository",
    resourceName = Some(this.getRepositoryName()),
    arnFormat = Some(ArnFormat.SLASH_RESOURCE_NAME),
  )

  override def addToResourcePolicy(statement: PolicyStatement): AddToResourcePolicyResult =
    AddToResourcePolicyResult.builder().statementAdded(false).build()
object PublicRepository:
  def fromRepositoryName(id: String, repositoryName: String)(using scope: Construct): PublicRepositoryBase =
    new PublicRepositoryBase(scope, id, null):
      given Stack = Stack of this

      override def getRepositoryName(): String = repositoryName
      override def getRepositoryArn(): String = buildArn(
        service = "ecr-public",
        region = Some(""),
        resource = "repository",
        resourceName = Some(repositoryName),
        arnFormat = Some(ArnFormat.SLASH_RESOURCE_NAME),
      )

      override def addToResourcePolicy(statement: PolicyStatement): AddToResourcePolicyResult =
        AddToResourcePolicyResult.builder().statementAdded(false).build()
