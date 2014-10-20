package org.bidpulse

import java.util.UUID.randomUUID

import akka.actor.{Kill, Terminated, PoisonPill, ActorSystem}
import akka.testkit._
import com.typesafe.config.ConfigFactory
import org.bidpulse.domain.{ProjectUpdate, InList, Trash, Projects}
import org.bidpulse.pipeline.Channel._
import org.bidpulse.pipeline.Channel
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ChannelTest(_system: ActorSystem) extends TestKit(_system) with ActorTestingUtils with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("ChannelTest", ConfigFactory.load(ConfigFactory.parseString("""
    akka.persistence.snapshot-store.local.dir = "target/snapshots"
    akka.persistence.journal.leveldb.dir = "target/journal"
    akka.persistence.journal.leveldb.native = off
  """))))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  override def commonTimeout = super.commonTimeout * 10

  "Channel" when {
    "subscribed (without requesting to send updates)" should {
      "replay published projects with applied updates" in {
        val channel = system.actorOf(Channel.props(randomUUID.toString))
        val project1 = generateProject
        val project2 = generateProject
        val project1Update = ProjectUpdate(project1.id, InList, Some(Trash))
        val updatedProject1 = project1.apply(project1Update)
        channel ! PublishProject(project1)
        channel ! PublishProject(project2)
        channel ! PublishProjectUpdate(project1Update)

        channel ! Channel.Subscribe(testActor, sendUpdates = false)
        expectMsg(commonTimeout, ProjectsPublished(Projects(Set(updatedProject1, project2))))
      }
    }

    "restarted and then subscribed (without requesting to send updates)" should {
      "replay published projects with applied updates" in {
        val persistenceId = randomUUID.toString
        var channel = system.actorOf(Channel.props(persistenceId))
        val project1 = generateProject
        val project2 = generateProject
        val project1Update = ProjectUpdate(project1.id, InList, Some(Trash))
        val updatedProject1 = project1.apply(project1Update)
        // send some initial data
        channel ! PublishProject(project1)
        channel ! PublishProject(project2)
        channel ! PublishProjectUpdate(project1Update)

        // verify that data has been persisted before terminating channel
        channel ! Channel.Subscribe(testActor, sendUpdates = false)
        expectMsg(commonTimeout, ProjectsPublished(Projects(Set(updatedProject1, project2))))

        // terminate channel
        watch(channel)
        channel ! PoisonPill
        expectMsgClass(commonTimeout, classOf[Terminated])

        // recreate channel and verify it still still holds valid data
        channel = system.actorOf(Channel.props(persistenceId))
        channel ! Channel.Subscribe(testActor, sendUpdates = false)
        expectMsg(commonTimeout, ProjectsPublished(Projects(Set(updatedProject1, project2))))
      }
    }

    "subscribed (with requesting to send updates)" should {
      "replay published projects and notify about new project" in {
        val channel = system.actorOf(Channel.props(randomUUID.toString))
        val project1 = generateProject
        val project2 = generateProject
        channel ! PublishProject(project1)

        channel ! Channel.Subscribe(testActor, sendUpdates = true)
        expectMsg(commonTimeout, ProjectsPublished(Projects(Set(project1))))

        channel ! PublishProject(project2)
        expectMsg(commonTimeout, ProjectPublished(project2))
      }

      "replay published projects and notify about project update" in {
        val channel = system.actorOf(Channel.props(randomUUID.toString))
        val project = generateProject
        val update = ProjectUpdate(project.id, InList, Some(Trash))
        channel ! PublishProject(project)

        channel ! Channel.Subscribe(testActor, sendUpdates = true)
        expectMsg(commonTimeout, ProjectsPublished(Projects(Set(project))))

        channel ! PublishProjectUpdate(update)
        expectMsg(commonTimeout, ProjectUpdatePublished(update))
      }
    }
  }

}