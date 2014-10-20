package org.bidpulse

import akka.actor.{Actor, Props}
import akka.testkit._
import scala.concurrent.duration._

trait ActorTestingUtils extends ImplicitSender {

  this: TestKit =>

  def dummy = Props(new Actor { def receive = { case _ => } })
  def resendToTestActor = Props(new Actor { def receive = { case msg => testActor ! msg } })

  def commonTimeout = (1 seconds).dilated

}
