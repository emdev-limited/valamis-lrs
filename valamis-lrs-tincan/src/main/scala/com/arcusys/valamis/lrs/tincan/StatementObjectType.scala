package com.arcusys.valamis.lrs.tincan

/**
 * Created by iliyatryapitsin on 26/12/14.
 */
object StatementObjectType extends Enumeration {
  type Type = Value
  
  val Activity           = Value(Constants.Tincan.Activity          )
  val Agent              = Value(Constants.Tincan.Agent             )
  val Group              = Value(Constants.Tincan.Group             )
  val Person             = Value(Constants.Tincan.Person            )
  val SubStatement       = Value(Constants.Tincan.SubStatement      )
  val StatementReference = Value(Constants.Tincan.StatementReference)
  
  def withType(obj: StatementObject) = 
    obj match {
      case _: Agent   => Agent
      case _: Group   => Group
      case _: Person  => Person
      case _: SubStatement => SubStatement
      case _: StatementReference => StatementReference
      case _: Activity => Activity
      case _ => throw new IllegalArgumentException("Unknown statement object type")
    }  
}
