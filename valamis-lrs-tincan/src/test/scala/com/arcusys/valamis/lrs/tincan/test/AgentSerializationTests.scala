package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.serializer.{AgentSerializer, SerializeFormat}
import com.arcusys.valamis.lrs.test.tincan.Agents
import com.arcusys.valamis.lrs.tincan.{Agent, FormatType}
import com.arcusys.valamis.utils.serialization.JsonHelper

/**
 * Created by Iliya Tryapitsin on 30/12/14.
 */
class AgentSerializationTests extends BaseSerializationTests {
  behavior of "Agent serializer/deserializer testing"

  it should "JSON with agent and objectType='Agent' deserialize agent correct" in {

    val raw = loadDataFromTxtResource("/agent-1.json")
    val agent = JsonHelper.fromJson[Agent](raw, new AgentSerializer())

    assert(None != agent.account)
    assert("http://www.example.com" == agent.account.get.homePage)
    assert("1625378" == agent.account.get.name)

    val json = JsonHelper.toJson(agent, new AgentSerializer(SerializeFormat(FormatType.Ids)))
    val json2 = JsonHelper.toJson(agent, new AgentSerializer)

    assert(!json.isEmpty)
    assert(!json2.isEmpty)
  }

  it should "JSON with agent and objectType='' deserialize correct" in {

    val raw = loadDataFromTxtResource("/agent-2.json")
    val agent = JsonHelper.fromJson[Agent](raw, new AgentSerializer())

    assert(None != agent.account)
    assert("http://www.example.com" == agent.account.get.homePage)
    assert("1625378" == agent.account.get.name)

    val json = JsonHelper.toJson(agent, new AgentSerializer(SerializeFormat(FormatType.Ids)))
    val json2 = JsonHelper.toJson(agent, new AgentSerializer)

    assert(!json.isEmpty)
    assert(!json2.isEmpty)
  }

  it should "JSON with agent and no objectType deserialize correct" in {

    val raw = loadDataFromTxtResource("/agent-3.json")
    val agent = JsonHelper.fromJson[Agent](raw, new AgentSerializer())

    assert(agent.mBox.isDefined)
    assert("mailto:test@test.com" == agent.mBox.get)

    val json = JsonHelper.toJson(agent, new AgentSerializer(SerializeFormat(FormatType.Ids)))
    val json2 = JsonHelper.toJson(agent, new AgentSerializer)

    assert(!json.isEmpty)
    assert(!json2.isEmpty)

    assert(json.contains("\"objectType\":\"Agent\""))
    assert(json2.contains("\"objectType\":\"Agent\""))
  }

  it should "JSON case #4 deserialize correct" in {

    val raw = loadDataFromTxtResource("/agent-4.json")
    val agent = JsonHelper.fromJson[Agent](raw, new AgentSerializer())

    assert(agent.mBox.isDefined)
    assert("mailto:test@test.com" == agent.mBox.get)
    
    val json = JsonHelper.toJson(agent, new AgentSerializer(SerializeFormat(FormatType.Ids)))
    val json2 = JsonHelper.toJson(agent, new AgentSerializer)

    assert(!json.isEmpty)
    assert(!json2.isEmpty)

    assert(json.contains("\"objectType\":\"Agent\""))
    assert(json2.contains("\"objectType\":\"Agent\""))
  }

  it should "serialize/deserialize agents" in {

    val typical = JsonHelper.toJson(Agents.typical)
    val typicalAgent = JsonHelper.fromJson[Agent](typical, new AgentSerializer)
    val typicalAgentSerialized = JsonHelper.toJson(typicalAgent, new AgentSerializer)
    assert(typical == typicalAgentSerialized)

    val mboxAndType = JsonHelper.toJson(Agents.mboxAndType)
    val mboxAndTypeAgent = JsonHelper.fromJson[Agent](mboxAndType, new AgentSerializer)
    val mboxAndTypeAgentSerialized = JsonHelper.toJson(mboxAndTypeAgent, new AgentSerializer)
    assert(mboxAndType == mboxAndTypeAgentSerialized)

    val mboxSha1AndType = JsonHelper.toJson(Agents.mboxSha1AndType)
    val mboxSha1AndTypeAgent = JsonHelper.fromJson[Agent](mboxSha1AndType, new AgentSerializer)
    val mboxSha1AndTypeAgentSerialized = JsonHelper.toJson(mboxSha1AndTypeAgent, new AgentSerializer)
    assert(mboxSha1AndType == mboxSha1AndTypeAgentSerialized)

    val openidAndType = JsonHelper.toJson(Agents.openidAndType)
    val openidAndTypeAgent = JsonHelper.fromJson[Agent](openidAndType, new AgentSerializer)
    val openidAndTypeAgentSerialized = JsonHelper.toJson(openidAndTypeAgent, new AgentSerializer)
    assert(openidAndType == openidAndTypeAgentSerialized)

    val accountAndType = JsonHelper.toJson(Agents.accountAndType)
    val accountAndTypeAgent = JsonHelper.fromJson[Agent](accountAndType, new AgentSerializer)
    val accountAndTypeAgentSerialized = JsonHelper.toJson(accountAndTypeAgent, new AgentSerializer)
    assert(accountAndType == accountAndTypeAgentSerialized)

    val mboxOnly = JsonHelper.toJson(Agents.mboxOnly)
    val mboxOnlyAgent = JsonHelper.fromJson[Agent](mboxOnly, new AgentSerializer)
    val mboxOnlyAgentSerialized = JsonHelper.toJson(mboxOnlyAgent, new AgentSerializer)

    val mboxSha1Only = JsonHelper.toJson(Agents.mboxSha1Only)
    val mboxSha1OnlyAgent = JsonHelper.fromJson[Agent](mboxSha1Only, new AgentSerializer)
    val mboxSha1OnlyAgentSerialized = JsonHelper.toJson(mboxSha1OnlyAgent, new AgentSerializer)

    val openidOnly = JsonHelper.toJson(Agents.openidOnly)
    val openidOnlyAgent = JsonHelper.fromJson[Agent](openidOnly, new AgentSerializer)
    val openidOnlyAgentSerialized = JsonHelper.toJson(openidOnlyAgent, new AgentSerializer)

    val accountOnly = JsonHelper.toJson(Agents.accountOnly)
    val accountOnlyAgent = JsonHelper.fromJson[Agent](accountOnly, new AgentSerializer)
    val accountOnlySerialized = JsonHelper.toJson(accountOnlyAgent, new AgentSerializer)
  }
}
