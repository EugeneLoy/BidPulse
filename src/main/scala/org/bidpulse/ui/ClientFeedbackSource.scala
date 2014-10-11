package org.bidpulse.ui

import org.bidpulse.domain.ProjectUpdate

object ClientFeedbackSource {

  sealed trait Command

  case class PublishProjectUpdate(update: ProjectUpdate) extends Command

}
