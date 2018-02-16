package com.arcusys.valamis.lrs.jdbc.history.ver230.from

  import com.arcusys.valamis.lrs.jdbc.history.ver230.from.row.AttachmentRow
import com.arcusys.valamis.lrs.tincan.Attachment

import scala.slick.jdbc._
import com.arcusys.valamis.lrs.jdbc.history.BaseComponent

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
trait AttachmentComponent { this: BaseComponent =>
  import com.arcusys.valamis.lrs.jdbc.history.Helper._

  private implicit val getResult = GetResult(r => {
    AttachmentRow(
      r.nextLong(),               //`id_` bigint(20) NOT NULL,
      r.nextLongOption(),         //`parentID` int(11) DEFAULT NULL,
      r.nextStringOption(),       //`usageType` longtext,
      r.nextStringOption(),       //`display` longtext,
      r.nextStringOption(),       //`description` longtext,
      r.nextStringOption(),       //`contentType` longtext,
      r.nextLongOption(),         //`length` int(11) DEFAULT NULL,
      r.nextStringOption(),       //`sha2` longtext,
      r.nextStringOption()        //`fileUrl` longtext,
    )
  })

  protected def attachments(implicit session: JdbcBackend#Session) = loadAttachments

  def loadAttachments(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[AttachmentRow](s"select * from ${getTableName("Learn_LFTincanLrsAttachment")}")
    .list

  protected def getAttachments(key: Long)(implicit session: JdbcBackend#Session): Seq[Attachment] = {
    attachments
      .filter(x => x.parentId.isDefined && x.parentId.get == key)
      .map { x =>
      Attachment(
        x.usageType.getOrElse(""),
        getLanguageMapOption(x.display).get,
        getLanguageMapOption(x.description),
        x.contentType.getOrElse(""),
        x.length.map { x => x.toInt}.getOrElse(0),
        x.sha2.getOrElse(""),
        x.fileUrl
      )
    }
  }
}