package org.bidpulse

import akka.actor.Actor.Receive
import akka.actor.{Actor, Props, ActorSystem}
import akka.testkit._
import org.bidpulse.pipeline.{Channel, Pipeline, Source}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike, Matchers, WordSpec}
import scala.concurrent.duration._

class PipelineTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("PipelineTest"))

  def dummy = Props(new Actor { def receive = { case _ => } })
  def resendToTestActor = Props(new Actor { def receive = { case msg => testActor ! msg } })

  def commonTimeout = (500 milliseconds).dilated

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Pipeline" when {
    "created" should {
      "create and initialize Channel" in {
        system.actorOf(Pipeline.props(resendToTestActor, Map.empty[String, Props]))

        expectMsgClass(commonTimeout, classOf[Channel.Init])
      }

      "create and initialize Sources" in {
        system.actorOf(Pipeline.props(dummy, Map("source1" -> resendToTestActor, "source2" -> resendToTestActor)))

        within(commonTimeout) {
          expectMsgClass(classOf[Source.Init])
          expectMsgClass(classOf[Source.Init])
        }
      }
    }

    "received subscription request" should {
      "subscribe publisher to channel" in {
        val publisher = TestProbe()
        val pipeline = system.actorOf(Pipeline.props(resendToTestActor, Map.empty[String, Props]))
        publisher.send(pipeline, Pipeline.Subscribe)

        within(commonTimeout) {
          expectMsgClass(classOf[Channel.Init])
          expectMsg(Channel.Subscribe(publisher.ref, true))
        }
      }
    }
  }

  it should {
    "correctly track publishers" in {
      val publisher = TestProbe()
      val pipeline = TestActorRef[Pipeline](Pipeline.props(resendToTestActor, Map.empty[String, Props]))
      publisher.send(pipeline, Pipeline.Subscribe)
      within(commonTimeout) {
        expectMsgClass(classOf[Channel.Init])
        expectMsgClass(classOf[Channel.Subscribe])
      }

      pipeline.underlyingActor.publishers should contain (publisher.ref)

      system.stop(publisher.ref)
      Thread.sleep(500) // TODO handle this without blocking

      pipeline.underlyingActor.publishers should not contain (publisher.ref)
    }
  }

}