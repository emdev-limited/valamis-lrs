package com.arcusys.valamis.lrs.jdbc.history.ver230.from.row

import java.util.UUID

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */


case class AgentProfileRow(key: Long,
                           documentId: Option[UUID],
                           agentId: Option[Long],
                           profileId: Option[String])