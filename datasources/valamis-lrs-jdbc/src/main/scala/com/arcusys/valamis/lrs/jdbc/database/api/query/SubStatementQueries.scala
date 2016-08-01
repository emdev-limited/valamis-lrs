package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait SubStatementQueries extends TypeAliases {
  this: LrsDataContext =>

  import driver.simple._

  private type ActorKeyCol = ConstColumn[ActorRow#Type]
  private type KeyCol      = ConstColumn[SubStatementRow#Type]

  /**
   * Search Sub Statement by Actor Key
   * @param actorKey Actor Key
   * @return Sub Statement storage query
   */
  def findSubStatementByActorIdQ (actorKey: ActorKeyCol) =
    subStatements filter { x =>
      x.actorKey           === actorKey ||
      x.statementObjectKey === actorKey
    }

  /**
   * Search Sub Statement by storage key
   * @param key Key
   * @return Sub Statement storage query
   */
  def findSubStatementByKeyQ (key: KeyCol) =
    subStatements filter {
      x => x.key === key
    }

  def findSubStatementsByKeysQ (keys: Seq[SubStatementRow#Type]) =
    subStatements filter {
      x => x.key inSet keys
    }

  /**
   * Compiled query for search Sub Statement keys by Actor Key
   */
  val findSubStatementKeysByActorIdQC = Compiled ((key: ActorKeyCol) =>
    findSubStatementByActorIdQ (key) map { x => x.key })

  /**
   * Compiled query for search Sub Statement records by Actor Key
   */
  val findSubStatementByKeyQC = Compiled ((key: KeyCol) =>
    findSubStatementByKeyQ (key))
}
