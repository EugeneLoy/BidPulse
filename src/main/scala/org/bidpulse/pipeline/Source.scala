package org.bidpulse.pipeline

object Source {

  sealed trait Command

  case class Init() extends Command

}

trait Source {

}
