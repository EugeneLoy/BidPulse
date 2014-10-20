package org.bidpulse.pipeline

import akka.actor.{ActorLogging, Props, Actor, ActorRef}
import akka.persistence.PersistentActor
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

  def props(persistenceId: String) = Props(classOf[Channel], persistenceId)

}

class Channel(_persistenceId: String) extends PersistentActor with ActorLogging {

  import Channel._

  var subscribers = Set.empty[ActorRef]
  var projects = Projects()

  override def persistenceId = _persistenceId

  val receiveRecover: Receive = {
    case ProjectPublished(project) =>
      projects += project
    case ProjectUpdatePublished(update) =>
      projects ~= update
  }

  val receiveCommand: Receive = {
    case Subscribe(subscriber, false) =>
      subscriber ! ProjectsPublished(projects)
    case Subscribe(subscriber, true) =>
      subscriber ! ProjectsPublished(projects)
      subscribers += subscriber
    case PublishProject(project) =>
      persist(ProjectPublished(project)) { event =>
        projects += event.project
        subscribers.foreach(_ ! event)
      }
    case PublishProjectUpdate(update) if projects contains update.projectId =>
      persist(ProjectUpdatePublished(update)) { event =>
        projects ~= event.update
        subscribers.foreach(_ ! event)
      }
  }

  // TODO add snapshots

}
