package com.arcusys.valamis.lrs.liferay.test.sqlserver

import com.arcusys.valamis.lrs.liferay.test.BaseCoreModule
import com.arcusys.valamis.lrs.test.config.MsSqlServerDbInit


/**
 * Created by Iliya Tryapitsin on 10/01/15.
 */
object SqlServerCoreModule extends BaseCoreModule(MsSqlServerDbInit())