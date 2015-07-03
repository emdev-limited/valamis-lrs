package com.arcusys.valamis.lrs.liferay.history.ver240

import com.arcusys.valamis.lrs.liferay.history.ver240.from.DataContext
import com.arcusys.valamis.lrs.services.LRS
import com.arcusys.valamis.lrs.liferay.history.BaseUpgrade

class DataUpgrade(val lrs: LRS) extends  BaseUpgrade{
  val dataContext = new DataContext

  def upgrade = lrs.db.withSession { implicit session =>{
    tryAction(dataContext.encodeResults())
  }}
}

