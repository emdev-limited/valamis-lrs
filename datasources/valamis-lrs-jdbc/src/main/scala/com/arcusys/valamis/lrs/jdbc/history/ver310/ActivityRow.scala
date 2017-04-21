/**
 * Created by Iliya Tryapitsin on 03/01/15.
 */

package com.arcusys.valamis.lrs.jdbc.history.ver310

import com.arcusys.valamis.lrs.jdbc.database.row.{StatementObjectRow, WithRequireKey}
import com.arcusys.valamis.lrs.tincan._

case class ActivityRow(key: StatementObjectRow#Type,
                       id: Option[String],
                       name: Option[LanguageMap] = None,
                       description: Option[LanguageMap] = None,
                       theType: Option[String] = None,
                       moreInfo: Option[String] = None,
                       interactionType: Option[InteractionType.Type] = None,
                       correctResponsesPattern: Seq[String] = Seq(),
                       choices: Seq[InteractionComponent] = Seq(),
                       scale: Seq[InteractionComponent] = Seq(),
                       source: Seq[InteractionComponent] = Seq(),
                       target: Seq[InteractionComponent] = Seq(),
                       steps: Seq[InteractionComponent] = Seq(),
                       extensions: Option[ExtensionMap] = None) extends WithRequireKey[StatementObjectRow#Type] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[Type])
}