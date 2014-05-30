import akka.cluster.Cluster
import akka.contrib.pattern.ClusterReceptionistExtension
import akka.routing.FromConfig
import com.typesafe.config.{ConfigMergeable, ConfigFactory}
import akka.actor.{Props, Actor, ActorLogging, ActorSystem}


object ComputeNode {
  def main(args: Array[String]): Unit = {
    val cmdLineConfig =
      ConfigFactory.parseString(
        List(
          "akka.remote.netty.tcp.port",
          "akka.remote.netty.tcp.hostname"
        )
        .zip(args)
        .map(_.productIterator.mkString("="))
        .mkString("\n")
      )

    val config =
      cmdLineConfig.withFallback(
        ConfigFactory.load("computeNode").withFallback(
          ConfigFactory.load("akkaCommon")
        )
      )

    val system = ActorSystem("ClusterSystem", config)
    val cluster = Cluster(system)

    val clusterReceptionist = ClusterReceptionistExtension(system)

    cluster.registerOnMemberUp {
      val computeRouter = system.actorOf(FromConfig.props(Props[ComputeActor]), "computeNodeRouter")
      clusterReceptionist.registerService(computeRouter)
    }
  }
}
