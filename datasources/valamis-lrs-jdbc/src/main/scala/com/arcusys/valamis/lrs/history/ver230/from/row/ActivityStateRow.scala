package com.arcusys.valamis.lrs.history.ver230.from.row

import java.util.UUID

/**
 * Created by Iliya Tryapitsin on 26/03/15.
 */
case class ActivityStateRow(id: Long,
                            stateId: Option[String],
                            documentId: Option[UUID],
                            activityId: Option[String],
                            registration: Option[String],
                            agentId: Option[Long])