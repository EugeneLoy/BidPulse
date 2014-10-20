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
      case ProjectUpdate(`id`, InList, Some(value: List)) => copy(inList = value)
    }
  }

  /**
   * Represents projects known to the system at some point in time.
   */
  case class Projects(projects: Set[Project] = Set.empty) {

    def contains(id: ProjectId) = projects.exists(_.id == id)

    /**
     * Add project to the known projects.
     */
    def +(project: Project) = {
      if (contains(project.id)) throw new IllegalArgumentException(s"Project: $project already exists in Projects: $this")
      else Projects(projects + project)
    }

    /**
     * Apply project update to known project.
     */
    def ~(update: ProjectUpdate[_]) = (projects.toList.partition(_.id == update.projectId): @unchecked) match {
      case (project :: Nil, rest) =>
        Projects(rest.toSet + project.apply(update))
      case (Nil, _) =>
        throw new IllegalArgumentException(s"Update: $update cannot be applied. Project does not exist in Projects: $this")
    }

  }

  /**
   * Represents update to the field of some project.
   * @param field updated field identifier
   * @param value updated field value
   * @tparam T type of the field
   */
  case class ProjectUpdate[T](projectId: ProjectId, field: UpdateableField, value: Option[T])

}
