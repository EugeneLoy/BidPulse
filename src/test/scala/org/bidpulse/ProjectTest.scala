package org.bidpulse

import org.bidpulse.domain._
import org.scalatest.{Matchers, WordSpec}


class ProjectTest extends WordSpec with Matchers {

  "Project" when {
    val project = Project(FreelancerId("id"), null, Unread, None, None, None, None, None, None, None, None, None)

    "applied with update" should {
      "return updated project" in {
        val updated = project.apply(ProjectUpdate(InList, Some(Todo)))
        updated.inList should equal (Todo)
      }
    }

    "applied with invalid inList update" should {
      "throw an exception" in intercept[Exception] {
        project.apply(ProjectUpdate(InList, None))
      }
    }

    "applied with unexpected field update" should {
      "throw an exception" in intercept[Exception] {
        project.apply(ProjectUpdate(null: UpdateableField, None))
      }
    }

  }

}
