package org.bidpulse.example

import java.util.UUID

import akka.actor.{ActorLogging, Actor, ActorRef, Cancellable}
import org.bidpulse.domain.{FreelancerId, Project, Unread}
import org.bidpulse.pipeline.Source.Init
import org.bidpulse.pipeline.{Channel, Source}

import scala.concurrent.duration._

class DummySource extends Actor with Source with ActorLogging {

  import context.dispatcher

  var channel: ActorRef = null
  var subscription: Option[Cancellable] = None

  override def receive: Receive = {
    case Init(ch) =>
      // initialise - subscribe to channel
      channel = ch
      channel ! Channel.Subscribe(self, false)
    case Channel.ProjectsPublished(_) =>
      // recover source state using replayed info from channel

      // simulate new project arrival every 5 secs
      subscription = Some(context.system.scheduler.schedule(5 seconds, 5 seconds) {
        val project = Project(
          FreelancerId(UUID.randomUUID.toString),
          null,
          Unread,
          None,
          None,
          None,
          None,
          None,
          None,
          None,
          None,
          None
        )
        channel.tell(Channel.PublishProject(project), self)
      })
  }

  override def postStop(): Unit = {
    for (s <- subscription) s.cancel()
    super.postStop
  }

}
