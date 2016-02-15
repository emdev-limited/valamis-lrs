package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.ActivityRow

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait ActivityQueries extends TypeAliases {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  private type KeyCol = ConstColumn[ActivityRow#Type]
  private type UriCol = ConstColumn[String]

  /**
   * Find activity by URI
   * @param uri URI
   * @return Activity query filtered by id
   */
  def findActivityQ (uri: UriCol) =
    activities filter { x =>
      x.id === uri
    }

  /**
   * Find activity by storage key
   * @param key Storage key
   * @return Activity query filtered by key
   */
  def findActivityByKeyQ (key: KeyCol) =
    activities filter {
      x => x.key === key
    }

  def findActivitiesByKeysQ (keys: Seq[ActivityRow#Type]) =
    activities filter {
      x => x.key inSet keys
    }

  /**
   * Find activity by URI compiled query
   */
  val findActivityByIdQC    = Compiled((uri: UriCol) => findActivityQ(uri))

  /**
   * Find activity by URI compiled query return only keys
   */
  val findActivityKeyByIdQC = Compiled((uri: UriCol) => findActivityQ(uri) map { x => x.key })

  /**
   * Find activity by storage key compiled query
   */
  val findActivityByKeyQC    = Compiled((key: KeyCol) => findActivityByKeyQ(key))
}
