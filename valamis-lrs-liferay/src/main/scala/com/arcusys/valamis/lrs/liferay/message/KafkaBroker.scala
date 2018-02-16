package com.arcusys.valamis.lrs.liferay.message

import java.util.Properties

import com.arcusys.valamis.lrs.utils._
import org.apache.kafka.clients.producer._

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
class KafkaBroker extends Broker {
  private val serializerClassName = "org.apache.kafka.common.serialization.StringSerializer"

  override def !(msg: (String, String)): Unit = {
    val props = new Properties()
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, EmptyString)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,    serializerClassName)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,      serializerClassName)

    val producer = new KafkaProducer[Null, String](props)
    val message  = new ProducerRecord(msg._1, null, msg._2)
    producer.send(message)
  }
}
