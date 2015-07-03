package com.arcusys.valamis.lrs.liferay.history.ver230.from.row

import java.util.UUID

import com.arcusys.valamis.lrs.tincan.ContentType
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
case class DocumentRow(key: Long,
                       documentId: UUID,
                       update: DateTime,
                       content: String,
                       contextType: ContentType.Type)
