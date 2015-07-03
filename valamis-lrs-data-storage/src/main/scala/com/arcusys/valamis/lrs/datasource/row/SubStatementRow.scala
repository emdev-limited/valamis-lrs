package com.arcusys.valamis.lrs.datasource.row

import com.arcusys.valamis.lrs.datasource.WithRequireKey
import com.arcusys.valamis.lrs.tincan.LanguageMap

/**
 * Created by igorborisov on 19.01.15.
 */
case class SubStatementRow(key: StatementObjectRow#Type,
                           objectKey: StatementObjectRow#Type,
                           actorKey: ActorRow#Type,
                           verbId: String,
                           verbDisplay: LanguageMap) extends WithRequireKey[Long] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[Type])
}
