package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait AccountQueries extends TypeAliases {
  this: LrsDataContext =>

  import driver.simple._

  type StringCol = ConstColumn[String]

  def findKeyQ (name:     StringCol,
                homePage: StringCol) =
    accounts filter { x =>
      x.homepage === homePage &&
      x.name     === name
    } map {
      x => x.key
    }

  val findKeyQC = Compiled (
    (name:     StringCol,
     homePage: StringCol) => findKeyQ(name, homePage))
}
