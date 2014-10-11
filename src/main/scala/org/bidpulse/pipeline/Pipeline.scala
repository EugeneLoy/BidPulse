package org.bidpulse.pipeline

object Pipeline {

  sealed trait Command

  case object Subscribe extends Command

}
