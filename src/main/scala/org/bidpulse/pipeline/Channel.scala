package org.bidpulse.pipeline

import akka.actor.{Actor, ActorRef}
import org.bidpulse.domain.{Project, ProjectUpdate, Projects}

import scala.language.existentials

object Channel {

  sealed trait Command
  sealed trait Event

  case class Init() extends Command
  case class Subscribe(actor: ActorRef, sendUpdates: Boolean) extends Command
  case class PublishProject(project: Project) extends Command
  case class PublishProjectUpdate(update: ProjectUpdate[_]) extends Command

  case class ProjectsPublished(projects: Projects) extends Event
  case class ProjectPublished(project: Project) extends Event
  case class ProjectUpdatePublished(update: ProjectUpdate[_]) extends Event

}

class Channel extends Actor {

  import Channel._

  var subscribers = Set.empty[ActorRef]

  override def receive: Receive ={
    case Subscribe(subscriber, false) =>
      subscriber ! ProjectsPublished(Projects(Set.empty[Project]))
    case Subscribe(subscriber, true) =>
      subscriber ! ProjectsPublished(Projects(Set.empty[Project]))
      subscribers += subscriber
    case PublishProject(project) =>
      subscribers.foreach(_ ! ProjectPublished(project))
  }

}
