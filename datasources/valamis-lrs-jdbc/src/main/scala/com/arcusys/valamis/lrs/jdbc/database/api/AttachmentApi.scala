package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs.jdbc.database.api.query.TypeAliases
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.TypeAliases
import com.arcusys.valamis.lrs.jdbc.database.row.AttachmentRow

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait AttachmentApi extends TypeAliases {
  this: LrsDataContext
    with SubStatementApi
    with ActorApi
    with ActivityApi =>

  import driver.simple._

  implicit class AttachmentInsertQueries (q: AttachmentQ) {

    /**
     * Insert new attachment record to data storage
     * @param rec Record instance
     * @param session
     * @return Storage key
     */
    def add (rec: AttachmentRow)
            (implicit session: Session): AttachmentRow#Type =
      q returning q.map { x =>
        x.key
      } += rec

    /**
     * Bulk insert attachment records to data storage
     * @param rec Record instance
     * @param session
     * @return List of keys
     */
    def addSeq (rec: Seq[AttachmentRow])
               (implicit session: Session): Seq[AttachmentRow#Type] =
      q returning q.map { x =>
        x.key
      } ++= rec
  }
}
