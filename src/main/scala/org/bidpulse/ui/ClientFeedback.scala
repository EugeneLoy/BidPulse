package org.bidpulse.ui

import org.bidpulse.domain.ProjectUpdate

object ClientFeedback {

  // TODO decice, maybe it is worth implementing both ClientFeedback and ClientPublisher as one actor

  sealed trait Event

  // TODO this event is a dto coming from websocket and it is ok to rewise it to fit serialization needs
  case class ProjectUpdatePublished(update: ProjectUpdate) extends Event

}
