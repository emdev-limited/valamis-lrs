package com.arcusys.valamis.lrs.tincan

/**
 * Created by iliyatryapitsin on 26/12/14.
 */

object ContentType extends Enumeration {
  type Type = Value

  val json = Value(Constants.Content.Json)
  val other = Value(Constants.Content.Other)

  def apply(value:String) = if(value.isEmpty) ContentType.other
  else if(value.toLowerCase.contains("json")) ContentType.json
  else ContentType.other

}