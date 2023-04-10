package io.ct2.cdkhelper.iam

import software.amazon.awscdk.services.iam.IPrincipal
import software.amazon.awscdk.services.iam.Role as JavaRole
import software.amazon.awscdk.services.iam.RoleProps
import software.constructs.Construct

final class Role(
    id: String,
    roleName: String,
    path: Option[String] = None,
    assumedBy: Option[IPrincipal] = None,
)(using scope: Construct)
    extends JavaRole(
      scope,
      id, {
        val props = RoleProps.builder().roleName(roleName)
        for r <- path do props path r
        for r <- assumedBy do props assumedBy r
        props.build()
      },
    )
