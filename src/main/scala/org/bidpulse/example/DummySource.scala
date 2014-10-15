package org.bidpulse.example

import akka.actor.{ActorLogging, Actor, ActorRef, Cancellable}
import org.bidpulse.domain.{FreelancerId, Project, Unread}
import org.bidpulse.pipeline.Source.Init
import org.bidpulse.pipeline.{Channel, Source}

import scala.concurrent.duration._

class DummySource extends Actor with Source with ActorLogging {

  var channel: ActorRef = null
  var subscription: Cancellable = null

  implicit val global = scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {
    case Init(ch) =>
      channel = ch
      channel ! Channel.Subscribe(self, false)
    case Channel.ProjectsPublished(_) =>
      val msg = Channel.PublishProject(Project(FreelancerId("id"), null, Unread, None, None, None, None, None, None, None, None, None))
      subscription = context.system.scheduler.schedule(5 seconds, 5 seconds) {
        channel ! msg
      }
  }

  override def postStop(): Unit = {
    subscription match {
      case null =>
      case sub =>
        sub.cancel
    }
    super.postStop
  }

}
