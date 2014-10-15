package org.bidpulse

import org.joda.time.DateTime

package object domain {

  /**
   * Represents list the project can be placed in.
   */
  sealed trait List
  case object Unread extends List
  case object Todo extends List
  case object Archive extends List
  case object Trash extends List

  /**
   * Represents updateable field of the project.
   */
  sealed trait UpdateableField
  case object InList extends UpdateableField
  // expect other project fields to be added

  /**
   * Represents id of the project. Typically holds info about origin of the project and native id relevant to that
   * origin.
   */
  sealed trait ProjectId
  case class FreelancerId(id: String) extends ProjectId
  // expect other project id types to be added

  /**
   * Represents project.
   */
  case class Project(
    id: ProjectId,
    title: String,
    description: String,
    stateTimestamp: DateTime, // last known create/update time on the project origin-side
    inList: List,
    createdAt: Option[DateTime],
    closingAt: Option[DateTime],
    status: Option[String],
    pricingType: Option[String],
    budgetFrom: Option[Double],
    budgetTo: Option[Double],
    budgetCurrency: Option[String],
    averageBid: Option[Double],
    country: Option[String]
    // expect more fields to be added
  ) {
    def apply(update: ProjectUpdate[_]): Project = update match {
      case ProjectUpdate(InList, Some(value: List)) => copy(inList = value)
    }
  }

  /**
   * Represents projects known to the system at some point in time.
   */
  case class Projects(projects: Set[Project])

  /**
   * Represents update to the field of some project.
   * @param field updated field identifier
   * @param value updated field value
   * @tparam T type of the field
   */
  case class ProjectUpdate[T](field: UpdateableField, value: Option[T])

}
