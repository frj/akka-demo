import akka.actor._
import akka.contrib.pattern.ClusterClient
import akka.pattern.AskSupport
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Client {
  def main(args: Array[String]): Unit = {
    val akkaConfig = ConfigFactory.load("client")
    val system = ActorSystem("Client", akkaConfig)
    val initialPointOfContact = system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/receptionist")
    val clusterClientRef = system.actorOf(ClusterClient.props(Set(initialPointOfContact)))
    val clientActorRef = system.actorOf(ClientActor.props(clusterClientRef))

    clientActorRef ! Test
  }
}

class ClientActor(clusterClientRef: ActorRef) extends Actor with AskSupport with ActorLogging {
  def receive: Actor.Receive = {
    case Test => 1 to 10 foreach { i =>
      val addMessage = AddMessage(i, 1)
      log.info(s"Sending Add request $addMessage")
      ask(clusterClientRef, ClusterClient.Send("/user/computeNodeRouter", addMessage, localAffinity = false))(Timeout(10 seconds))
        .mapTo[AddResponse]
        .foreach { response =>
          log.info(s"Received add response $response")
        }
    }
  }
}

object ClientActor {
  def props(clusterClientRef: ActorRef) = Props(new ClientActor(clusterClientRef))
}

case class Test()