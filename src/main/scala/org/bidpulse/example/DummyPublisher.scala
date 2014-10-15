package org.bidpulse.example

import akka.actor.{Actor, ActorLogging, ActorRef}
import org.bidpulse.pipeline.{Channel, Pipeline, Publisher}


class DummyPublisher(pipeline: ActorRef) extends Actor with Publisher with ActorLogging {


  pipeline ! Pipeline.Subscribe


  override def receive: Receive = {
    case msg: Channel.ProjectsPublished =>
      log.info(s"message published $msg")
    case msg: Channel.ProjectPublished =>
      log.info(s"message published $msg")
    case msg: Channel.ProjectUpdatePublished =>
      log.info(s"message published $msg")
  }

}
