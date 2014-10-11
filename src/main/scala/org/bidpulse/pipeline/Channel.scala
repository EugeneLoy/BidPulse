package org.bidpulse.pipeline

import akka.actor.ActorRef
import org.bidpulse.domain.{Projects, ProjectUpdate, Project}

object Channel {

  sealed trait Command
  sealed trait Event

  case class Subscribe(actor: ActorRef, sendUpdates: Boolean) extends Command
  case class PublishProject(project: Project) extends Command
  case class PublishProjectUpdate(update: ProjectUpdate) extends Command

  case class ProjectsPublished(projects: Projects) extends Event
  case class ProjectPublished(project: Project) extends Event
  case class ProjectUpdatePublished(update: ProjectUpdate) extends Event

}
