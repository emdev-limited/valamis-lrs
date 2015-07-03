package com.arcusys.valamis.lrs.tincan

/**
 *
 * @param id A value such as used in practice for "cmi.interactions.n.id"
 *           as defined in the SCORM 2004 4th Edition Run-Time Environment
 * @param description A description of the interaction component
 *                    (for example, the text for a given choice in a multiple-choice interaction)
 */
case class InteractionComponent(id: String,
                                description: LanguageMap) {
  override def toString =
    s"""
       |InteractionComponent instance
       |id          = $id
       |description = $description
     """.stripMargin
}
