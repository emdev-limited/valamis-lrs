package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs.jdbc.database.api.query.TypeAliases
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.{VerbQueries, TypeAliases}

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait VerbApi extends VerbQueries {
  this: LrsDataContext
    with ResultApi
    with ScoreApi
    with StatementObjectApi
    with AttachmentApi
    with AccountApi
    with ContextApi
    with SubStatementApi
    with StatementRefApi
    with ActorApi
    with ActivityApi
    with TypeAliases =>

}
