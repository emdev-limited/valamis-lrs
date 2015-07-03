package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 01/04/15.
 */
object Helper {
  def getFields(o: Any): Map[String, Any] = {
    val fieldsAsPairs = for (field <- o.getClass.getDeclaredFields if field.getName != "MODULE$") yield {
      field.setAccessible(true)
      (field.getName, field.get(o))
    }
    Map(fieldsAsPairs :_*)
  }

  implicit def any2FieldValues[A](o: A): Object {def fieldValues: Map[String, Any]} = new AnyRef {
    def fieldValues = getFields(o)
  }
}
