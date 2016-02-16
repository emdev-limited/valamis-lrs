package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.ActorRow

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait AgentProfileQueries extends TypeAliases {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  private type ActorKeyCol  = ConstColumn[ActorRow#Type]
  private type ProfileIdCol = ConstColumn[String]

  private def findByActorAndProfileIdQ (actorKey:  ActorKeyCol,
                                        profileId: ProfileIdCol) =
    agentProfiles filter { x =>
      x.profileId  === profileId &&
      x.agentKey   === actorKey
    }

  val findAgentProfilesByActorAndProfileIdQC = Compiled(
    (actorKey:  ActorKeyCol,
     profileId: ProfileIdCol) => findByActorAndProfileIdQ(actorKey, profileId))

}
