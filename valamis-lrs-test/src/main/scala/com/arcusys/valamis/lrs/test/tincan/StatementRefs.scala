package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */

case class StatementRef(id: Option[String] = None,
                        objectType: Option[String] = None) extends StatementObj

object StatementRefs {
  val empty         = Some(StatementRef())
  val typical       = Some(StatementRef(Some("16fd2706-8baf-433b-82eb-8c7fada847da"), Some("StatementRef")))
  val allProperties = Some(StatementRef(Some("16fd2706-8baf-433b-82eb-8c7fada847da"), Some("StatementRef")))
}
