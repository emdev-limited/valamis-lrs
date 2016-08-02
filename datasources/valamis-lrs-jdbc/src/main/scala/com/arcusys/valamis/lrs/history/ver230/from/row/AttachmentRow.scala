package com.arcusys.valamis.lrs.history.ver230.from.row

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */


case class AttachmentRow(key: Long,
                         parentId: Option[Long],
                         usageType: Option[String],
                         display: Option[String],
                         description: Option[String],
                         contentType: Option[String],
                         length: Option[Long],
                         sha2: Option[String],
                         fileUrl: Option[String])