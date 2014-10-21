package org.bidpulse.pipeline

import akka.actor.ActorRef

object Source {

  sealed trait Command

  case class Init(channel: ActorRef) extends Command


}

trait Source
