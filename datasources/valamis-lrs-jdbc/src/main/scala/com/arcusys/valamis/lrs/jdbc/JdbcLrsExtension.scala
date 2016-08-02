package com.arcusys.valamis.lrs.jdbc

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api._

trait JdbcLrsExtension
  extends LrsDataContext
  with ActivityProfileApi
  with StatementApi
  with AgentComponent
  with ActivityProfileComponent
  with StateProfileComponent
  with AgentProfileApi
  with DocumentApi
  with ResultApi
  with ScoreApi
  with StatementObjectApi
  with AccountApi
  with ContextApi
  with SubStatementApi
  with StatementRefApi
  with ActorApi
  with ActivityApi
  with Loggable