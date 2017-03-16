package com.knoldus.mapping

import com.knoldus.connection.DBComponent
import com.knoldus.model.Dependent


trait DependentTable extends EmployeeTable {

  this: DBComponent =>

  import driver.api._

  protected class DependentTable(tag: Tag) extends Table[Dependent](tag, "dependent") {

    val id = column[Int]("dependentid", O.PrimaryKey, O.AutoInc)
    val empId = column[Int]("empid")
    val name = column[String]("name")
    val relation = column[String]("relation")
    val age = column[Option[Int]]("age", O.Default(None))

    def employeeDependentFK = foreignKey("employee-dependent_fk", empId, employeeTableQuery)(_.id)

    def * = (id, empId, name, relation, age) <>(Dependent.tupled, Dependent.unapply)

  }

  protected val dependentTableQuery = TableQuery[DependentTable]

  protected def dependentTableAutoInc = dependentTableQuery returning dependentTableQuery.map(_.id)

  protected def dependentTableObject = dependentTableQuery returning dependentTableQuery.map(identity)

}
