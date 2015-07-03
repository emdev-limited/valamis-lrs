package com.arcusys.valamis.lrs.datasource.row

import java.util.UUID

import com.arcusys.valamis.lrs.datasource.WithRequireKey
import com.arcusys.valamis.lrs.tincan.ContentType.Type
import com.arcusys.valamis.lrs.tincan.Document
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 13/01/15.
 */
case class DocumentRow(key: DocumentRow#Type = UUID.randomUUID.toString,
                       updated: DateTime = new DateTime(),
                       contents: String, // TODO Should be binary
                       cType: Type) extends WithRequireKey[String] {

  override def withId[M](e: M) = copy(key = e.asInstanceOf[Type])

  def toModel = Document(UUID.fromString(key), updated, contents, cType)
}
