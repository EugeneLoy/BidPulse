package org.bidpulse

import java.util.UUID.randomUUID

import org.bidpulse.domain._
import org.scalatest.{Matchers, WordSpec}

class ProjectsTest extends WordSpec with Matchers {

  "Projects" when {
    val project1 = generateProject
    val project2 = generateProject
    val project1Update = ProjectUpdate(project1.id, InList, Some(Trash))
    val updatedProject1 = project1.apply(project1Update)

    "created" should {
      "contain no projects" in {
        val projects = Projects()
        projects.projects should be (empty)
      }
    }

    "added a project" should {
      "tell that it contains only added project" in {
        var projects = Projects()
        projects += project1

        projects.contains(project1.id) should be (true)
        projects.contains(project2.id) should be (false)
      }
    }

    "added the same project multiple times" should {
      "throw an exception" in {
        var projects = Projects()
        projects += project1

        intercept[Exception] {
          projects += project1
        }
      }
    }

    "added few projects" should {
      "contain all added projects" in {
        var projects = Projects()
        projects += project1
        projects += project2

        projects.projects should equal (Set(project1, project2))
      }
    }

    "added few projects and update" should {
      "contain added projects (with applied updates)" in {
        var projects = Projects()
        projects += project1
        projects += project2
        projects ~= project1Update

        projects.projects should equal (Set(updatedProject1, project2))
      }
    }

    "added the update for unknown project" should {
      "throw an exception" in {
        var projects = Projects()
        projects += project2

        intercept[Exception] {
          projects ~= project1Update
        }
      }
    }

  }

}
