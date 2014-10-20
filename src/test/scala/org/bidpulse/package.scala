package org

import java.util.UUID.randomUUID

import org.bidpulse.domain.{Project, Unread, FreelancerId}

package object bidpulse {

  def generateProject = Project(FreelancerId(randomUUID.toString), "", "", null, Unread, None, None, None, None, None, None, None, None, None)

}
