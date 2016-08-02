package com.arcusys.valamis.lrs.test.h2

import com.arcusys.valamis.lrs.test.BaseCoreModule
import com.arcusys.valamis.lrs.test.config.H2DbInit


/**
 * Created by Iliya Tryapitsin on 10/01/15.
 */
object H2CoreModule extends BaseCoreModule(H2DbInit())
