package io.ct2.cdkhelper.ssm

import io.ct2.cdkhelper.buildArn
import io.ct2.cdkhelper.iam
import java.util as ju
import scala.annotation.threadUnsafe
import scala.jdk.CollectionConverters.given
import software.amazon.awscdk.ArnFormat
import software.amazon.awscdk.IResolvable
import software.amazon.awscdk.IResolveContext
import software.amazon.awscdk.Resource
import software.amazon.awscdk.ResourceProps
import software.amazon.awscdk.Stack
import software.amazon.awscdk.services.iam.Grant
import software.amazon.awscdk.services.iam.IGrantable
import software.amazon.awscdk.services.ssm.CfnDocument
import software.amazon.awscdk.services.ssm.CfnDocumentProps
import software.constructs.Construct

sealed class Document(
    id: String,
    content: Object,
    documentType: Option[String] = None,
    name: String,
    targetType: Option[String] = None,
    updateMethod: Option[String] = None,
)(using scope: Construct)
    extends Resource(scope, id, ResourceProps.builder().physicalName(name).build()):
  given Stack = Stack of this

  private val resource =
    val props = CfnDocumentProps.builder().name(name).content(content)
    for r <- documentType do props documentType r
    for r <- targetType do props targetType r
    for r <- updateMethod do props updateMethod r
    CfnDocument(this, "Resource", props.build())

  @threadUnsafe lazy val documentArn = buildArn(
    service = "ssm",
    resource = "document",
    resourceName = Some(this.resource.getRef()),
    arnFormat = Some(ArnFormat.SLASH_RESOURCE_NAME),
  )

  def grantSendCommand(grantee: IGrantable): Grant = iam.grant(
    grantee = grantee,
    actions = Seq("ssm:SendCommand"),
    resourceArns = Seq(this.documentArn),
    scope = this,
  )

abstract sealed class CommandStep extends IResolvable:
  private val creationStack = Thread.currentThread.getStackTrace.map { _.toString() }.toList.asJava
  override def getCreationStack(): ju.List[String] = creationStack
object CommandStep:
  final class RunShellScript(val name: String, val workingDirectory: Option[String] = None, val runCommands: String*)
      extends CommandStep:
    override def resolve(context: IResolveContext): Object =
      var inputs: Map[String, Object] = Map("runCommand" -> runCommands.asJava)
      inputs = workingDirectory map { inputs.updated("workingDirectory", _) } getOrElse inputs

      Map(
        "action" -> "aws:runShellScript",
        "name" -> name,
        "inputs" -> inputs.asJava,
      ).asJava

final class CommandDocumentSteps(private val steps: Object*) extends IResolvable:
  private val creationStack = Thread.currentThread.getStackTrace.map { t => t.toString() }.toList.asJava

  override def resolve(_context: IResolveContext): Object = this.steps.asJava
  override def getCreationStack(): ju.List[String] = this.creationStack

final class CommandDocument(
    id: String,
    name: String,
    schemaVersion: String = "2.2",
    description: String = "",
    mainSteps: CommandDocumentSteps = CommandDocumentSteps(),
    targetType: Option[String] = None,
    updateMethod: Option[String] = None,
)(using Construct)
    extends Document(
      id,
      content = Map("schemaVersion" -> schemaVersion, "description" -> description, "mainSteps" -> mainSteps).asJava,
      documentType = Some("Command"),
      name = name,
      targetType = targetType,
      updateMethod = updateMethod,
    )
