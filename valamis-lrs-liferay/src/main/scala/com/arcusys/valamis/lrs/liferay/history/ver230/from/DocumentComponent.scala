package com.arcusys.valamis.lrs.liferay.history.ver230.from

import java.util.UUID

  import com.arcusys.valamis.lrs.liferay.history.ver230.from.row.DocumentRow
import com.arcusys.valamis.lrs.tincan.{ContentType, Document}

import scala.slick.jdbc._
import com.arcusys.valamis.lrs.liferay.history.BaseComponent
import com.arcusys.valamis.lrs.liferay.history.Helper._

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
trait DocumentComponent { this: BaseComponent =>

  private implicit val getResult = GetResult(r => {
    DocumentRow(
      r.nextLong(),                   //`id_` bigint(20) NOT NULL,
      getUUIDOrRandom(r.nextStringOption()),  //`documentId` varchar(512) DEFAULT NULL,
      getDateTimeOrNow(r.nextDateOption()),   //`update_` datetime DEFAULT NULL,
      r.nextString(),       //`content` longtext,
      r.nextStringOption() match {
        case None => ContentType.other
        case Some(value) => ContentType(value)
      }  //`contentType` varchar(2000) DEFAULT NULL,
    )
  })

  protected def documents(implicit session: JdbcBackend#Session) = loadDocuments

  def loadDocuments(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[DocumentRow](s"select * from ${getTableName("Learn_LFTincanLrsDocument")}")
    .list

  protected def getDocument(id: UUID)(implicit session: JdbcBackend#Session): Document = {
    val doc = documents.find(x => x.documentId == id)

    require(doc.isDefined, s"Cann't find document with id = $id")
    Document(doc.get.documentId, doc.get.update, doc.get.content, doc.get.contextType)
  }

  protected def getDocument(key: Long)(implicit session: JdbcBackend#Session): Document = {
    val doc = documents.find(x => x.key == key)

    require(doc.isDefined, s"Cann't find document with key = $key")
    Document(doc.get.documentId, doc.get.update, doc.get.content, doc.get.contextType)
  }
}
