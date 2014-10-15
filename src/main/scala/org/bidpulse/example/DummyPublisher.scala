package org.bidpulse.example

import akka.actor.{Actor, ActorLogging, ActorRef}
import org.bidpulse.pipeline.{Channel, Pipeline, Publisher}


class DummyPublisher(pipeline: ActorRef) extends Actor with Publisher with ActorLogging {

  pipeline ! Pipeline.Subscribe

  override def receive: Receive = {
    case msg @ (_: Channel.ProjectsPublished | _: Channel.ProjectPublished | _: Channel.ProjectUpdatePublished) =>
      log.info(s"Message published\n>>>> $msg")
  }

}
