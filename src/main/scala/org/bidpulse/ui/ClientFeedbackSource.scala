package org.bidpulse.ui

import org.bidpulse.domain.ProjectUpdate
import scala.language.existentials

object ClientFeedbackSource {

  sealed trait Command

  case class PublishProjectUpdate(update: ProjectUpdate[_]) extends Command

}
