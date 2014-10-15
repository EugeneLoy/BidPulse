package org.bidpulse.pipeline

import akka.actor.ActorRef
import org.bidpulse.domain.{Projects, ProjectUpdate, Project}
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
