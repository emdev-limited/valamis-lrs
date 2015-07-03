package com.arcusys.valamis.lrs.datasource.row

import com.arcusys.valamis.lrs.datasource.WithOptionKey
import com.arcusys.valamis.lrs.tincan.LanguageMap

/**
 * Created by Iliya Tryapitsin on 02/01/15.
 */
case class AttachmentRow(key: AttachmentRow#KeyType = None,
                         statementId: StatementRow#Type,
                         usageType: String,
                         display: LanguageMap,
                         description: Option[LanguageMap] = None,
                         content: String,
                         length: Int,
                         sha2: String,
                         fileUrl: Option[String]) extends WithOptionKey[Long] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[KeyType])
}