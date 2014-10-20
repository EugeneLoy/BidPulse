package org.bidpulse

import org.bidpulse.domain._
import org.scalatest.{Matchers, WordSpec}
import java.util.UUID.randomUUID

class ProjectTest extends WordSpec with Matchers {

  "Project" when {
    val project = generateProject

    "applied with update" should {
      "return updated project" in {
        val updated = project.apply(ProjectUpdate(project.id, InList, Some(Todo)))
        updated.inList should equal (Todo)
      }
    }

    "applied with invalid inList update" should {
      "throw an exception" in intercept[Exception] {
        project.apply(ProjectUpdate(project.id, InList, None))
      }
    }

    "applied with invalid id update" should {
      "throw an exception" in intercept[Exception] {
        project.apply(ProjectUpdate(FreelancerId(randomUUID.toString), InList, Some(Todo)))
      }
    }

    "applied with unexpected field update" should {
      "throw an exception" in intercept[Exception] {
        project.apply(ProjectUpdate(project.id, null: UpdateableField, None))
      }
    }

  }

}
