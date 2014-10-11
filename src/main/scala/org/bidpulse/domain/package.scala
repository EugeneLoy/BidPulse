package org.bidpulse

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
    val id: ProjectId,
    val inList: List
    // expect more fields to be added
  )

  /**
   * Represents projects known to the system at some point in time.
   */
  case class Projects(projects: Set[Project])

  /**
   * Represents update to the field of some project.
   * @param field
   * @param value
   * @tparam T
   */
  case class ProjectUpdate[T](field: UpdateableField, value: Option[T])

}
