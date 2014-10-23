package org.bidpulse.server

import akka.actor.{Actor, Props, ActorRef}
import spray.can.websocket
import spray.can.websocket.FrameCommandFailed
import spray.can.websocket.frame.TextFrame

object WorkerService {

  def props(serverConnection: ActorRef) = Props(classOf[WorkerService], serverConnection)

}

class WorkerService(val serverConnection: ActorRef) extends Actor with RoutedService with websocket.WebSocketServerWorker {

  override def actorRefFactory = context

  override def businessLogic: Receive = {
    case x: TextFrame =>
      log.info(x.toString)
      sender ! x
    case x: FrameCommandFailed =>
      // TODO figure out how to handle this
      log.error("frame command failed", x)
  }

  override def receive = handshaking orElse routing orElse closeLogic

}