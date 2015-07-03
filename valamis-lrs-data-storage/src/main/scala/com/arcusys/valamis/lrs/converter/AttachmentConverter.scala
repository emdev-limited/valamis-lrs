package com.arcusys.valamis.lrs.converter

import com.arcusys.valamis.lrs.datasource.row.{AttachmentRow, StatementRow}
import com.arcusys.valamis.lrs.tincan.Attachment

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
object AttachmentConverter {
  implicit def asRow(value: Attachment, 
                     statementId: StatementRow#Type): AttachmentRow = AttachmentRow(None,
    statementId, 
    value.usageType,
    value.display,
    value.description,
    value.contentType,
    value.length,
    value.sha2,
    value.fileUrl)

  implicit def asModel(value: AttachmentRow): Attachment = Attachment(
    value.usageType,
    value.display,
    value.description,
    value.content,
    value.length,
    value.sha2,
    value.fileUrl)
}
