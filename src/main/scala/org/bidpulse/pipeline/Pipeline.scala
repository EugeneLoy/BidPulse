package org.bidpulse.pipeline

import java.util.UUID.randomUUID

import akka.actor.{Terminated, ActorRef, Props, Actor}
import akka.actor.Actor.Receive

object Pipeline {

  sealed trait Command

  case object Subscribe extends Command

  def props(channelProps: Props, sourcesProps: Map[String, Props]) = Props(classOf[Pipeline], channelProps, sourcesProps)

}

class Pipeline(channelProps: Props, sourcesProps: Map[String, Props]) extends Actor {

  import Pipeline._

  val channel = context.actorOf(channelProps, "channel")
  channel ! Channel.Init()

  val sources = for {
    (name, props) <- sourcesProps
  } yield context.actorOf(props, s"${name}_${randomUUID}")

  sources.foreach(_ ! Source.Init(channel))

  var publishers = Set.empty[ActorRef]

  override def receive: Receive = {
    case Subscribe =>
      publishers += context.watch(sender)
      channel ! Channel.Subscribe(sender, sendUpdates = true)
    case Terminated(publisher) if publishers contains publisher =>
      publishers -= publisher
  }

  // TODO implement error recovery

}