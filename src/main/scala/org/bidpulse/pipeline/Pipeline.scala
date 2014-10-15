package org.bidpulse.pipeline

import java.util.UUID

import akka.actor.{Props, Actor}
import akka.actor.Actor.Receive

object Pipeline {

  sealed trait Command

  case object Subscribe extends Command

  def props(channelProps: Props, sourcesProps: Map[String, Props]) = Props(classOf[Pipeline], channelProps, sourcesProps)

}

class Pipeline(channelProps: Props, sourcesProps: Map[String, Props]) extends Actor {

  import Pipeline._

  val channel = context.actorOf(channelProps, s"channel_${UUID.randomUUID.toString}")
  channel ! Channel.Init()

  val sources = for {
    (name, props) <- sourcesProps
  } yield context.actorOf(props, s"${name}_${UUID.randomUUID.toString}")

  sources.foreach(_ ! Source.Init())

  override def receive: Receive = {
    case _ =>
  }

  // TODO implement error recovery

}