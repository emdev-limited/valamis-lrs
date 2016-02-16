package com.arcusys.valamis.lrs.jdbc.database.row

import java.util.UUID
import com.arcusys.valamis.lrs.tincan.ExtensionMap

/**
 * Created by Iliya Tryapitsin on 11/01/15.
 */
case class ContextRow(registration: ContextRow#KeyType = Some(UUID.randomUUID.toString),
                      instructor: Option[ActorRow#Type] = None,
                      team: Option[GroupRow#Type] = None,
                      revision: Option[String] = None,
                      platform: Option[String] = None,
                      language: Option[String] = None,
                      statement: Option[StatementReferenceRow#Type],
                      extensions: Option[ExtensionMap] = None) extends WithOptionKey[String] {

  override val key: KeyType = registration

  override def withId[M](e: M) = copy(registration = key)
}