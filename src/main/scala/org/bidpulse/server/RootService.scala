package org.bidpulse.server

import java.util.UUID.randomUUID

import akka.actor.{ActorLogging, Actor, Props}
import spray.can.Http

object RootService {

  def props = Props(classOf[RootService])

}

class RootService extends Actor with ActorLogging {

  def receive = {
    case Http.Connected(remoteAddress, localAddress) =>
      val serverConnection = sender()
      val conn = context.actorOf(WorkerService.props(serverConnection), s"worker_$randomUUID")
      serverConnection ! Http.Register(conn)
  }

}