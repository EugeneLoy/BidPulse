package org.bidpulse

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import org.bidpulse.example.{DummyPublisher, DummySource}
import org.bidpulse.pipeline.{Channel, Pipeline}
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("bidpulse")

  val pipeline = system.actorOf(Pipeline.props(Props[Channel], Map("dummy_source" -> Props[DummySource])))
  system.actorOf(Props(classOf[DummyPublisher], pipeline))

  // create and start our service actor
  val service = system.actorOf(Props[ServiceActor], "service")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
