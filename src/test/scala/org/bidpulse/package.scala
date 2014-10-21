package org

import java.util.UUID.randomUUID

import akka.actor._
import akka.testkit.{TestKit, ImplicitSender}
import org.bidpulse.domain.{Project, Unread, FreelancerId}
import akka.testkit._
import org.scalatest.{Suite, BeforeAndAfterAll}
import scala.concurrent.duration._

package object bidpulse {

  def generateProject = Project(FreelancerId(randomUUID.toString), "", "", null, Unread, None, None, None, None, None, None, None, None, None)

  abstract class  ActorTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with Suite with BeforeAndAfterAll {

    override def afterAll = {
      TestKit.shutdownActorSystem(system)
    }

    def dummy = Props(new Actor { def receive = { case _: Any => } })
    def resendToTestActor = Props(new Actor { def receive = { case msg => testActor ! msg } })

    def commonTimeout = (1 seconds).dilated

  }

}
