package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.serializer._
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.utils.serialization.JsonHelper

class ActorSerializationTests extends BaseSerializationTests {
  behavior of "Actor serializer/deserializer testing"

  it should "JSON with agent and objectType='Agent' deserialize agent from actor correct" in {
    val raw = loadDataFromTxtResource("/agent-1.json")
    val actor = JsonHelper.fromJson[Actor](raw, new ActorSerializer())

    assert(actor.isInstanceOf[Actor])

    val ag = actor.asInstanceOf[Actor]
    assert(None != ag.account)
    assert("http://www.example.com" == ag.account.get.homePage)
    assert("1625378" == ag.account.get.name)

    val json = JsonHelper.toJson(actor, new ActorSerializer)
    val json2 = JsonHelper.toJson(actor, new ActorSerializer)

    assert(!json.isEmpty)
    assert(!json2.isEmpty)
  }

  it should "JSON with agent and objectType='' deserialize correct" in {
    val json = loadDataFromTxtResource("/agent-2.json")
    val actor = JsonHelper.fromJson[Actor](json, new ActorSerializer())

    assert(actor.isInstanceOf[Actor])

    val ag = actor.asInstanceOf[Actor]
    assert(None != ag.account)
    assert("http://www.example.com" == ag.account.get.homePage)
    assert("1625378" == ag.account.get.name)
  }

  it should "JSON with agent and no objectType deserialize correct" in {
    val raw = loadDataFromTxtResource("/agent-3.json")
    val actor = JsonHelper.fromJson[Actor](raw, new ActorSerializer())

    assert(actor.isInstanceOf[Actor])

    val ag = actor.asInstanceOf[Actor]
    assert(ag.mBox.isDefined)
    assert("mailto:test@test.com" == ag.mBox.get)
  }
}