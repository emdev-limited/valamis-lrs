package com.arcusys.valamis.lrs.tincan

import com.arcusys.valamis.lrs.validator.VerbValidator

/**
 * The Verb defines the action between Actor and Activity.
 * @param id Corresponds to a Verb definition. Each Verb definition corresponds to the meaning of a Verb, not the word.
 *           The IRI should be human-readable and contain the Verb meaning.
 * @param display The human readable representation of the Verb in one or more languages.
 *                This does not have any impact on the meaning of the Statement,
 *                but serves to give a human-readable display of the meaning already determined by the chosen Verb.
 */
case class Verb(id:      Verb#Id,
                display: LanguageMap) {

  type Id = String

  VerbValidator checkRequirements this

  def isVoided = id.equals ("http://adlnet.gov/expapi/verbs/voided") ||
    id.equals("http://adlnet.gov/expapi/verbs/voided/")

  override def equals(that: Any) = that match {
    case t: Verb =>
      id      .equals ( t.id      ) &&
      display .equals ( t.display )

    case _ => false
  }

  override def hashCode = id.hashCode() + display.hashCode()

  override def toString =
    s"""
       |Verb instance
       |id          = $id
       |display     = $display
     """.stripMargin
}
