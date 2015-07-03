package com.arcusys.valamis.lrs.tincan

/**
 * A Statement may represent an Activity as the Object of the Statement.
 * The following table lists the Object properties in this case
 * @param id An identifier for a single unique Activity. Required.
 * @param name The human readable/visual name of the Activity
 * @param description  A description of the Activity
 * @param theType The type of Activity.
 * @param moreInfo SHOULD resolve to a document human-readable information about the Activity,
 *                 which MAY include a way to 'launch' the Activity.
 * @param interactionType As in "cmi.interactions.n.type" as defined in the SCORM 2004 4th Edition Run-Time Environment.
 * @param correctResponsesPattern Corresponds to "cmi.interactions.n.correct_responses.n.pattern"
 *                                as defined in the SCORM 2004 4th Edition Run-Time Environment,
 *                                where the final n is the index of the array.
 * @param choices Specific to the given interactionType
 * @param scale Specific to the given interactionType
 * @param source Specific to the given interactionType
 * @param target Specific to the given interactionType
 * @param steps Specific to the given interactionType
 * @param extensions A map of other properties as needed
 */
case class Activity(id: String,
                    name: Option[LanguageMap] = None,
                    description: Option[LanguageMap] = None,
                    theType: Option[String] = None,
                    moreInfo: Option[String] = None,
                    interactionType: Option[InteractionType.Value] = None,
                    correctResponsesPattern: Seq[String] = Seq(),
                    choices: Seq[InteractionComponent] = Seq(),
                    scale: Seq[InteractionComponent] = Seq(),
                    source: Seq[InteractionComponent] = Seq(),
                    target: Seq[InteractionComponent] = Seq(),
                    steps: Seq[InteractionComponent] = Seq(),
                    extensions: Option[Map[String, String]] = None) extends StatementObject {
  override def toString =
    s"""
       |Activity instance
       |id                          = $id
       |name                        = $name
       |description                 = $description
       |theType                     = $theType
       |moreInfo                    = $moreInfo
       |interactionType             = $interactionType
       |correctResponsesPattern     = $correctResponsesPattern
       |choices                     = $choices
       |scale                       = $scale
       |source                      = $source
       |target                      = $target
       |steps                       = $steps
       |extensions                  = $extensions
     """.stripMargin
}