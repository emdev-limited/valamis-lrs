package com.arcusys.valamis.lrs

import java.net.URI
import java.util.UUID

import com.arcusys.valamis.lrs.tincan.{Actor, FormatType, Statement}
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 26/01/15.
 */

/**
 *
 * @param statementId ID of Statement to fetch
 * @param voidedStatementId ID of voided Statement to fetch.
 * @param agent Filter, only return Statements for which the specified Agent or group is the Actor or Object of the Statement.
 *              Agents or identified groups are equal when the same Inverse Functional Identifier is
 *              used in each Object compared and those Inverse Functional Identifiers have equal values.
 *              For the purposes of this filter, groups that have members which match the specified Agent
 *              based on their Inverse Functional Identifier as described above are considered a match
 * @param verb Filter, only return Statements matching the specified verb id.
 * @param activity Filter, only return Statements for which the Object of the Statement is an Activity with the specified id.
 * @param registration Filter, only return Statements matching the specified registration id.
 *                     Note that although frequently a unique registration id will be used for one Actor assigned to one Activity, this cannot be assumed.
 *                     If only Statements for a certain Actor or Activity are required, those parameters also need to be specified.
 * @param since Only Statements stored since the specified timestamp (exclusive) are returned.
 * @param until Only Statements stored at or before the specified timestamp are returned.
 * @param relatedActivities Apply the Activity filter broadly. Include Statements for which the Object, any of the context Activities,
 *                          or any of those properties in a contained SubStatement match the Activity parameter, instead of that parameter's normal behavior.
 *                          Matching is defined in the same way it is for the 'activity' parameter.
 * @param relatedAgents Apply the Agent filter broadly. Include Statements for which the Actor, Object,
 *                      Authority, Instructor, Team, or any of these properties in a contained SubStatement match
 *                      the Agent parameter, instead of that parameter's normal behavior.
 *                      Matching is defined in the same way it is for the 'agent' parameter.
 * @param limit Maximum number of Statements to return. 0 indicates return the maximum the server will allow.
 * @param format If "ids", only include minimum information necessary in Agent, Activity, Verb and Group Objects to identify them.
 *               For anonymous groups this means including the minimum information needed to identify each member.
 *
 *               If "exact", return Agent, Activity, Verb and Group Objects populated exactly as they were when the Statement was received.
 *               An LRS requesting Statements for the purpose of importing them would use a format of "exact".
 *
 *               If "canonical", return Activity Objects and Verbs populated with the canonical definition of the
 *               Activity Objects and Display of the Verbs as determined by the LRS, after applying the language filtering
 *               process defined below, and return the original Agent and Group Objects as in "exact" mode.
 * @param attachments If true, the LRS uses the multipart response format and includes all attachments as described previously.
 *                    If false, the LRS sends the prescribed response with Content-Type application/json and cannot use attachments.
 * @param ascending If true, return results in ascending order of stored time
 */
case class StatementQuery(statementId: Statement#Id = None,
                          voidedStatementId: Statement#Id = None,
                          agent: Option[Actor] = None,
                          verb: Option[URI] = None,
                          activity: Option[URI] = None,
                          registration: Option[UUID] = None,
                          since: Option[DateTime] = None,
                          until: Option[DateTime] = None,
                          relatedActivities: Boolean = false,
                          relatedAgents: Boolean = false,
                          limit: Int = 100,
                          offset: Int = 0,
                          format: FormatType.Value = FormatType.Canonical,
                          attachments: Boolean = false,
                          ascending: Boolean = false) {

  if (statementId.isDefined && voidedStatementId.isDefined)
    throw new IllegalArgumentException("Both statement id and voided statement id were passed")

  if (limit < 0)
    throw new IllegalArgumentException("Parameter 'limit' must have non-negative integer value")
}
