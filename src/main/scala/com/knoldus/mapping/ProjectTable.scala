package com.knoldus.mapping

import com.knoldus.connection.DBComponent
import com.knoldus.model.Project


trait ProjectTable extends EmployeeTable {

  this: DBComponent =>

  import driver.api._

  protected class ProjectTable(tag: Tag) extends Table[Project](tag, "project") {

    val projId = column[Int]("projid", O.PrimaryKey)
    val name = column[String]("name")
    val empId = column[Int]("empId")
    val teamSize = column[Int]("teamsize")
    val teamLead = column[String]("teamlead")

    def employeeProjectFK = foreignKey("employee_project_fk", empId, employeeTableQuery)(_.id)

    def * = (projId, name, empId, teamSize, teamLead) <> (Project.tupled, Project.unapply)
  }

  protected val projectTableQuery = TableQuery[ProjectTable]

  protected def projectTableAutoInc = projectTableQuery returning projectTableQuery.map(_.projId)

}
