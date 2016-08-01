package com.arcusys.valamis.lrs.test.postgresql

import com.arcusys.valamis.lrs.test.BaseCoreModule
import com.arcusys.valamis.lrs.test.config.PostgresDbInit

/**
 * Created by Iliya Tryapitsin on 10/01/15.
 */
object PostgresCoreModule extends BaseCoreModule(PostgresDbInit())