package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.serializer.{ActorSerializer, GroupSerializer}
import com.arcusys.valamis.lrs.test.tincan.Groups
import com.arcusys.valamis.lrs.tincan.{Actor, Group}
import com.arcusys.valamis.utils.serialization.JsonHelper

/**
 * Created by Iliya Tryapitsin on 30/12/14.
 */
class GroupSerializationTests extends BaseSerializationTests {
  behavior of "Group serializer/deserializer testing"

  it should "JSON with group without members deserialize correct" in {

    val raw = loadDataFromTxtResource("/group-1.json")
    val group = JsonHelper.fromJson[Actor](raw, new ActorSerializer())

    assert(group.isInstanceOf[Group])

    val g = group.asInstanceOf[Group]
    assert(g.mBox.isDefined)
    assert("mailto:test@test.com" == g.mBox.get)
    assert(g.member.isDefined)

    val json = JsonHelper.toJson(group, new GroupSerializer)
    val json2 = JsonHelper.toJson(group, new GroupSerializer)

    assert(!json.isEmpty)
    assert(!json2.isEmpty)

    assert(json.contains("\"objectType\":\"Group\""))
    assert(json2.contains("\"objectType\":\"Group\""))
  }

  it should "JSON with group with members deserialize correct" in {

    val raw = loadDataFromTxtResource("/group-2.json")
    val group = JsonHelper.fromJson[Group](raw, new GroupSerializer())

    assert(group.isInstanceOf[Group])

    val g = group.asInstanceOf[Group]
    assert(g.mBox.isDefined)
    assert("mailto:test@test.com" == g.mBox.get)
    assert(3 == g.member.get.size)

    val json = JsonHelper.toJson(group, new GroupSerializer)
    val json2 = JsonHelper.toJson(group, new GroupSerializer)

    assert(!json.isEmpty)
    assert(!json2.isEmpty)

    assert(json.contains("\"objectType\":\"Group\""))
    assert(json2.contains("\"objectType\":\"Group\""))
  }

  it should "serialize/deserialize group" in {
    val typical = JsonHelper.toJson(Groups.typical)
    val typicalGroup = JsonHelper.fromJson[Group](typical, new GroupSerializer)
    val typicalGroupSerialized = JsonHelper.toJson(typicalGroup, new GroupSerializer)

    val mboxAndType = JsonHelper.toJson(Groups.mboxAndType)
    val mboxAndTypeGroup = JsonHelper.fromJson[Group](mboxAndType, new GroupSerializer)
    val mboxAndTypeGroupSerialized = JsonHelper.toJson(mboxAndTypeGroup, new GroupSerializer)

    val mboxSha1AndType = JsonHelper.toJson(Groups.mboxSha1AndType)
    val mboxSha1AndTypeGroup = JsonHelper.fromJson[Group](mboxSha1AndType, new GroupSerializer)
    val mboxSha1AndTypeGroupSerialized = JsonHelper.toJson(mboxSha1AndTypeGroup, new GroupSerializer)

    val openidAndType = JsonHelper.toJson(Groups.openidAndType)
    val openidAndTypeGroup = JsonHelper.fromJson[Group](openidAndType, new GroupSerializer)
    val openidAndTypeGroupSerialized = JsonHelper.toJson(openidAndTypeGroup, new GroupSerializer)

    val accountAndType = JsonHelper.toJson(Groups.accountAndType)
    val accountAndTypeGroup = JsonHelper.fromJson[Group](accountAndType, new GroupSerializer)
    val accountAndTypeGroupSerialized = JsonHelper.toJson(accountAndTypeGroup, new GroupSerializer)

    val mboxTypeAndMember = JsonHelper.toJson(Groups.mboxTypeAndMember)
    val mboxTypeAndMemberGroup = JsonHelper.fromJson[Group](mboxTypeAndMember, new GroupSerializer)
    val mboxTypeAndMemberGroupSerialized = JsonHelper.toJson(mboxTypeAndMemberGroup, new GroupSerializer)

    val mboxTypeAndName = JsonHelper.toJson(Groups.mboxTypeAndName)
    val mboxTypeAndNameGroup = JsonHelper.fromJson[Group](mboxTypeAndName, new GroupSerializer)
    val mboxTypeAndNameGroupSerialized = JsonHelper.toJson(mboxTypeAndNameGroup, new GroupSerializer)

    val mboxSha1TypeAndMember = JsonHelper.toJson(Groups.mboxSha1TypeAndMember)
    val mboxSha1TypeAndMemberGroup = JsonHelper.fromJson[Group](mboxSha1TypeAndMember, new GroupSerializer)
    val mboxSha1TypeAndMemberGroupSerialized = JsonHelper.toJson(mboxSha1TypeAndMemberGroup, new GroupSerializer)

    val mboxSha1TypeAndName = JsonHelper.toJson(Groups.mboxSha1TypeAndName)
    val mboxSha1TypeAndNameGroup = JsonHelper.fromJson[Group](mboxSha1TypeAndName, new GroupSerializer)
    val mboxSha1TypeAndNameGroupSerialized = JsonHelper.toJson(mboxSha1TypeAndNameGroup, new GroupSerializer)

    val openidTypeAndName = JsonHelper.toJson(Groups.openidTypeAndName)
    val openidTypeAndNameGroup = JsonHelper.fromJson[Group](openidTypeAndName, new GroupSerializer)
    val openidTypeAndNameGroupSerialized = JsonHelper.toJson(openidTypeAndNameGroup, new GroupSerializer)

    val openidTypeAndMember = JsonHelper.toJson(Groups.openidTypeAndMember)
    val openidTypeAndMemberGroup = JsonHelper.fromJson[Group](openidTypeAndMember, new GroupSerializer)
    val openidTypeAndMemberGroupSerialized = JsonHelper.toJson(openidTypeAndMemberGroup, new GroupSerializer)

    val accountTypeAndName = JsonHelper.toJson(Groups.accountTypeAndName)
    val accountTypeAndNameGroup = JsonHelper.fromJson[Group](accountTypeAndName, new GroupSerializer)
    val accountTypeAndNameGroupSerialized = JsonHelper.toJson(accountTypeAndNameGroup, new GroupSerializer)

    val accountTypeAndMember = JsonHelper.toJson(Groups.accountTypeAndMember)
    val accountTypeAndMemberGroup = JsonHelper.fromJson[Group](accountTypeAndMember, new GroupSerializer)
    val accountTypeAndMemberGroupSerialized = JsonHelper.toJson(accountTypeAndMemberGroup, new GroupSerializer)

    val allPropertiesTypicalAgentMember = JsonHelper.toJson(Groups.allPropertiesTypicalAgentMember)
    val allPropertiesTypicalAgentMemberGroup = JsonHelper.fromJson[Group](allPropertiesTypicalAgentMember, new GroupSerializer)
    val allPropertiesTypicalAgentMemberGroupSerialized = JsonHelper.toJson(allPropertiesTypicalAgentMemberGroup, new GroupSerializer)

    val allPropertiesMboxAgentMember = JsonHelper.toJson(Groups.allPropertiesMboxAgentMember)
    val allPropertiesMboxAgentMemberGroup = JsonHelper.fromJson[Group](allPropertiesMboxAgentMember, new GroupSerializer)
    val allPropertiesMboxAgentMemberGroupSerialized = JsonHelper.toJson(allPropertiesMboxAgentMemberGroup, new GroupSerializer)

    val allPropertiesMboxSha1AgentMember = JsonHelper.toJson(Groups.allPropertiesMboxSha1AgentMember)
    val allPropertiesMboxSha1AgentMemberGroup = JsonHelper.fromJson[Group](allPropertiesMboxSha1AgentMember, new GroupSerializer)
    val allPropertiesMboxSha1AgentMemberGroupSerialized = JsonHelper.toJson(allPropertiesMboxSha1AgentMemberGroup, new GroupSerializer)

    val allPropertiesOpenidAgentMember = JsonHelper.toJson(Groups.allPropertiesOpenidAgentMember)
    val allPropertiesOpenidAgentMemberGroup = JsonHelper.fromJson[Group](allPropertiesOpenidAgentMember, new GroupSerializer)
    val allPropertiesOpenidAgentMemberGroupSerialized = JsonHelper.toJson(allPropertiesOpenidAgentMemberGroup, new GroupSerializer)

    val allPropertiesAccountAgentMember = JsonHelper.toJson(Groups.allPropertiesAccountAgentMember)
    val allPropertiesAccountAgentMemberGroup = JsonHelper.fromJson[Group](allPropertiesAccountAgentMember, new GroupSerializer)
    val allPropertiesAccountAgentMemberGroupSerialized = JsonHelper.toJson(allPropertiesAccountAgentMemberGroup, new GroupSerializer)

    val allPropertiesTwoTypicalAgentsMember = JsonHelper.toJson(Groups.allPropertiesTwoTypicalAgentsMember)
    val allPropertiesTwoTypicalAgentsMemberGroup = JsonHelper.fromJson[Group](allPropertiesTwoTypicalAgentsMember, new GroupSerializer)
    val allPropertiesTwoTypicalAgentsMemberGroupSerialized = JsonHelper.toJson(allPropertiesTwoTypicalAgentsMemberGroup, new GroupSerializer)

  }
}
