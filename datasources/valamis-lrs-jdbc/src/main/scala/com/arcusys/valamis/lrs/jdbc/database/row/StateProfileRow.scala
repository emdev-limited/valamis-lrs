package com.arcusys.valamis.lrs.jdbc.database.row

case class StateProfileRow(stateId: String,
                        agentKey: AgentRow#Type,
                        activityKey: ActivityRow#Type,
                        registration: Option[String],
                        documentKey: DocumentRow#Type)
