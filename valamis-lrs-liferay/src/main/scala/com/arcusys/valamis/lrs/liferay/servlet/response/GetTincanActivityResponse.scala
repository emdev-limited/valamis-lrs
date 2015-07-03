package com.arcusys.valamis.lrs.liferay.servlet.response

import com.arcusys.valamis.lrs.tincan.Activity

/**
 * Created by iliyatryapitsin on 26/12/14.
 */

object GetTincanActivityResponse {
  def apply(act: Activity) = new GetTincanActivityResponse(act.id, if (act.name.isDefined) act.name.get.head._2 else act.id)

  def apply(id: String, name: String) = new GetTincanActivityResponse(id, name)
}

class GetTincanActivityResponse(val id: String,
                                val name: String)

