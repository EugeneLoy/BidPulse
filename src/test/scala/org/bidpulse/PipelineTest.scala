package org.bidpulse

import akka.actor.{Props, ActorSystem}
import akka.testkit._
import org.bidpulse.pipeline.{Channel, Pipeline, Source}
import org.scalatest.{WordSpecLike, Matchers}

class PipelineTest(_system: ActorSystem) extends ActorTest(_system) with WordSpecLike with Matchers {

  def this() = this(ActorSystem("PipelineTest"))

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
      val pipeline = TestActorRef[Pipeline](Pipeline.props(dummy, Map.empty[String, Props]))
      publisher.send(pipeline, Pipeline.Subscribe)

      pipeline.underlyingActor.publishers should contain (publisher.ref)

      pipeline.underlyingActor.context.stop(publisher.ref)

      pipeline.underlyingActor.publishers should not contain (publisher.ref)
    }
  }

}