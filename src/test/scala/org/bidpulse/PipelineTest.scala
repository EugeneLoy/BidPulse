package org.bidpulse

import akka.actor.Actor.Receive
import akka.actor.{Actor, Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.bidpulse.pipeline.{Channel, Pipeline, Source}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike, Matchers, WordSpec}
import scala.concurrent.duration._

class PipelineTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("PipelineTest"))

  def dummy = Props(new Actor { def receive = { case _ => } })
  def resendToTestActor = Props(new Actor { def receive = { case msg => testActor ! msg } })

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Pipeline" when {
    "created" should {
      "create and initialize Channel" in {
        system.actorOf(Pipeline.props(resendToTestActor, Map.empty[String, Props]))
        expectMsgClass(1 second, classOf[Channel.Init])
      }

      "create and initialize Sources" in {
        system.actorOf(Pipeline.props(dummy, Map("source1" -> resendToTestActor, "source2" -> resendToTestActor)))
        within(1 second) {
          expectMsgClass(classOf[Source.Init])
          expectMsgClass(classOf[Source.Init])
        }
      }
    }
  }

}