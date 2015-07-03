package com.arcusys.valamis.lrs.tincan

/**
 * Created by iliyatryapitsin on 26/12/14.
 */
object StatementObjectType extends Enumeration {
  type Type = Value
  
  val activity           = Value(Constants.Tincan.Activity          )
  val agent              = Value(Constants.Tincan.Agent             )
  val group              = Value(Constants.Tincan.Group             )
  val person             = Value(Constants.Tincan.Person            )
  val subStatement       = Value(Constants.Tincan.SubStatement      )
  val statementReference = Value(Constants.Tincan.StatementReference)
}
