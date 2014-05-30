import akka.actor.{ActorLogging, Actor}

class ComputeActor extends Actor with ActorLogging
{
  def receive: Actor.Receive = {
    case AddMessage(val1, val2) =>
      log.info(s"Computing addition of $val1 and $val2 from $sender()")
      sender() ! AddResponse(val1, val2, val1 + val2)
  }
}


