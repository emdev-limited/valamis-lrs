package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.{ActivityProfileQueries, ActorQueries}
import com.arcusys.valamis.lrs.tincan.Activity

/**
 * Created by Iliya Tryapitsin on 13.07.15.
 */
trait ActivityProfileApi  extends ActivityProfileQueries {
  this: LrsDataContext
    with StatementObjectApi
    with AccountApi =>

  import executionContext.driver.simple._

}
