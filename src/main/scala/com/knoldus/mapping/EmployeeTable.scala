package com.knoldus.mapping

import com.knoldus.connection.DBComponent
import com.knoldus.model.Employee


trait EmployeeTable {

  this: DBComponent =>

  import driver.api._

  protected class EmployeeTable(tag: Tag) extends Table[Employee](tag, "employee") {

    val id = column[Int]("id", O.PrimaryKey)
    val name = column[String]("name")
    val experience = column[Double]("experience")

    def * = (id, name, experience) <>(Employee.tupled, Employee.unapply)
  }

  protected val employeeTableQuery = TableQuery[EmployeeTable]

  protected def employeeTableAutoInc = employeeTableQuery returning employeeTableQuery.map(_.id)

}
