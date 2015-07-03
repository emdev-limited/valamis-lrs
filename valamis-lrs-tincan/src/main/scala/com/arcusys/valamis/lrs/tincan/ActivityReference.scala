package com.arcusys.valamis.lrs.tincan

/**
 * Created by Iliya Tryapitsin on 30/12/14.
 */
case class ActivityReference(id: String,
                             objectType: Option[StatementObjectType.Type] = None) {
  override def toString =
    s"""
       |ActivityReference instance
       |id          = $id
       |objectType  = $objectType
     """.stripMargin
}
