package org.bidpulse.example

import java.util.UUID.randomUUID

import akka.actor.{ActorLogging, Actor, ActorRef, Cancellable}
import org.bidpulse.domain.{FreelancerId, Project, Unread}
import org.bidpulse.pipeline.Source.Init
import org.bidpulse.pipeline.{Channel, Source}
import org.joda.time.DateTime

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
          FreelancerId(randomUUID.toString),
          s"Title $randomUUID",
          "Description",
          DateTime.now,
          Unread,
          Some(DateTime.now),
          Some(DateTime.now),
          Some("open"),
          Some("rate"),
          Some(1000),
          Some(10000),
          Some("$"),
          Some(5000),
          Some("New Zeland")
        )
        channel.tell(Channel.PublishProject(project), self)
      })
  }

  override def postStop(): Unit = {
    for (s <- subscription) s.cancel()
    super.postStop
  }

}
