package org

import java.util.UUID.randomUUID

import akka.actor.{Actor, Props}
import akka.testkit.{TestKit, ImplicitSender}
import org.bidpulse.domain.{Project, Unread, FreelancerId}
import akka.actor.{Actor, Props}
import akka.testkit._
import scala.concurrent.duration._

package object bidpulse {

  def generateProject = Project(FreelancerId(randomUUID.toString), "", "", null, Unread, None, None, None, None, None, None, None, None, None)

  trait ActorTestingUtils extends ImplicitSender {

    this: TestKit =>

    def dummy = Props(new Actor { def receive = { case _ => } })
    def resendToTestActor = Props(new Actor { def receive = { case msg => testActor ! msg } })

    def commonTimeout = (1 seconds).dilated

  }

}
