package com.arcusys.valamis.lrs.datasource.row

case class StateProfileRow(stateId: String,
                        agentKey: AgentRow#Type,
                        activityKey: ActivityRow#Type,
                        registration: Option[String],
                        documentKey: DocumentRow#Type)
