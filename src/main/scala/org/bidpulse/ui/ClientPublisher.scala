package org.bidpulse.ui

import org.bidpulse.domain.{Projects, ProjectUpdate, Project}

object ClientPublisher {

  sealed trait Command

  // TODO these commands are dtos that will be pushed to websocket and it is ok to rewise it to fit serialization needs
  case class PublishProjects(projects: Projects) extends Command
  case class PublishProject(project: Project) extends Command
  case class PublishProjectUpdate(update: ProjectUpdate) extends Command

}
