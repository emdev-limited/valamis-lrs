package com.arcusys.valamis.lrs.datasource.row

import java.util.UUID

import com.arcusys.valamis.lrs.datasource.WithRequireKey
import com.arcusys.valamis.lrs.tincan.{LanguageMap, TincanVersion}
import org.joda.time.DateTime

/**
 * Created by igorborisov on 12.01.15.
 */

case class StatementRow(key: StatementRow#Type = UUID.randomUUID.toString,
                        actorKey: ActorRow#Type,
                        verbId: String,
                        verbDisplay: LanguageMap,
                        objectKey: StatementObjectRow#Type,
                        resultKey: ResultRow#KeyType = None,
                        contextKey: ContextRow#KeyType = None,
                        timestamp: DateTime = DateTime.now,
                        stored: DateTime = DateTime.now,
                        authorityKey: Option[ActorRow#Type] = None,
                        version: Option[TincanVersion.Type] = None) extends WithRequireKey[String] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[Type])

}
