package com.arcusys.valamis.lrs.test.cluster

import java.io.IOException
import java.util.Properties

//import com.arcusys.valamis.lrs.test.config.TestConfig
import kafka.server.{KafkaConfig, KafkaServer}
import org.apache.zookeeper.server.{ServerConfig, ZooKeeperServerMain}
import org.apache.zookeeper.server.quorum.QuorumPeerConfig
import org.scalatest.FlatSpec

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
trait EmbeddedKafka extends FlatSpec {

  protected var kafkaServer: KafkaServer = _
//  protected val kafkaConfig     = TestConfig("test.conf", "kafka"    )
//  protected val zooKeeperConfig = TestConfig("test.conf", "zookeeper")

  "Run kafka embedded server" should "success" in {

    val props = new Properties()
    props.setProperty("zookeeper.connect", "localhost:9091")
    props.setProperty("hostname", "localhost")
    props.setProperty("port", "9090")
    props.setProperty("broker.id", "1")
    props.setProperty("log.dir", "/tmp/embeddedkafka/")

    val zkProps = new Properties()
    zkProps.setProperty("tickTime", "2000")
    zkProps.setProperty("dataDir", "/tmp/embeddedzookeeper")
    zkProps.setProperty("clientPort", "9091")

    startZookeeperLocal(zkProps)
    startKafkaLocal(props)

  }

  def startKafkaLocal(props: Properties): KafkaServer = {
    val server = new KafkaServer(new KafkaConfig(props))
    server.startup()
    server.awaitShutdown()
    server
  }

  def shutdownKafkaLocal(server: KafkaServer) = server.shutdown()

  def startZookeeperLocal(props: Properties): Unit = {
    val quorumConfiguration = new QuorumPeerConfig()
    try {
      quorumConfiguration.parseProperties(props)
    } catch {
      case e: Exception => throw new RuntimeException(e)
    }

    val zooKeeperServer = new ZooKeeperServerMain()
    val configuration = new ServerConfig()
    configuration.readFrom(quorumConfiguration)


    new Thread() {
      override def run() {
        try {
          zooKeeperServer.runFromConfig(configuration)
        } catch {
          case e: IOException =>
            println("ZooKeeper Failed")
            e.printStackTrace(System.err)
        }
      }
    } start
  }
}
