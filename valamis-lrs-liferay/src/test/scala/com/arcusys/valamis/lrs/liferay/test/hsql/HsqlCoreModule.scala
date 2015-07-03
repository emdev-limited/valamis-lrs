package com.arcusys.valamis.lrs.liferay.test.hsql

import com.arcusys.valamis.lrs.liferay.test.BaseCoreModule
import com.arcusys.valamis.lrs.test.config.HsqlDbInit


/**
 * Created by Iliya Tryapitsin on 10/01/15.
 */
object HsqlCoreModule extends BaseCoreModule(HsqlDbInit())
