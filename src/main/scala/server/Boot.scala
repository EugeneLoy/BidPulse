package server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import org.bidpulse.example.{DummyPublisher, DummySource}
import org.bidpulse.pipeline.{Channel, Pipeline}
import spray.can.Http
import spray.can.server.UHttp

import scala.concurrent.duration._

object Boot extends App {

  implicit val system = ActorSystem("system")

  // create/configure pipeline
  val pipeline = system.actorOf(Pipeline.props(Channel.props("channel-persistence-id") , Map("dummy_source" -> Props[DummySource])), "pipeline")

  // dummy publisher will simulate adding/updating projects
  system.actorOf(Props(classOf[DummyPublisher], pipeline))

  // create/start root service that will handle http/websockets
  val service = system.actorOf(RootService.props, "root_service")
  implicit val timeout = Timeout(5.seconds)
  IO(UHttp) ? Http.Bind(service, interface = "localhost", port = 8080)

}
