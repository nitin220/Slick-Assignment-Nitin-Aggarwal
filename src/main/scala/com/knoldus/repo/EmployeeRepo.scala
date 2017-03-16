package com.knoldus.repo

import com.knoldus.connection.{PostgresComponent, DBComponent}
import com.knoldus.mapping.{DependentTable, ProjectTable, EmployeeTable}
import com.knoldus.model.Employee
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait EmployeeRepo extends EmployeeTable with DependentTable{

  this: DBComponent =>

  import driver.api._

  /*
  * creating schema for the given table
  * */

  def create: Future[Unit] = db.run(employeeTableQuery.schema.create)

  /*
  * inserting a record
  * */

  def insert(newEmployee: Employee): Future[Int] = db.run {
    employeeTableQuery += newEmployee
  }


  /*
  * inserting a record and returning Id of inserted record
  * */

  def insertReturningId(newEmployee: Employee): Future[Int] =  {
    val action=employeeTableAutoInc += newEmployee
    db.run(action)
  }

  /*
  * inserting a record and returning inserted record
  * */

  def insertReturningObject(newEmployee: Employee): Future[Employee] =  {
   val action=employeeTableObject+=newEmployee
    db.run(action)
  }

  /*
  * inserting multiple records in sequence
  * */

  def insertMultiple(newEmployee: Employee,newEmp2:Employee): Future[Int] = {
    val action1:DBIO[Int]=employeeTableQuery += newEmployee
    val action2:DBIO[Int]=employeeTableQuery += newEmp2
    //val a4: DBIO[List[Int]] = DBIO.sequence(List(action1, action2)).cleanUp(x=>action1)
    val query=action1.andThen(action2).transactionally
    //db.run(a4)
    db.run(query)
  }

  /*
  * deleting a record on the basis of experience
  * */

  def deleteOnBasisOfExperience(experience: Double): Future[Int] = {
    val query = employeeTableQuery.filter(_.experience === experience).delete
    db.run(query)
  }

  /*
  * deleting a record on the basis of ID
  * */

  def deleteOnBasisOfId(id: Int): Future[Int] = {
    val query = employeeTableQuery.filter(_.id === id).delete
    db.run(query)
  }


  /*
  * deleting a record on the basis of Name
  * */

  def deleteOnBasisOfName(name: String): Future[Int] = {
    val query = employeeTableQuery.filter(_.name === name).delete
    db.run(query)
  }

  /*
  * Updating  the name field of the record for the given id
  * */

  def updateName(id: Int, name: String): Future[Int] = {
    val query = employeeTableQuery.filter(_.id === id).map(_.name).update(name)
    db.run(query)
  }

  /*
  * Updating  the experience field of the record for the given id
  * */

  def updateExperience(id: Int, experience: Double): Future[Int] = {
    val query = employeeTableQuery.filter(_.id === id).map(_.experience).update(experience)
    db.run(query)
  }

  /*
  * Insertion(if record is not present) or updation(if existing record) ie upsert
  * */

  def upsert(employee: Employee): Future[Int] = {
    val query = employeeTableQuery.insertOrUpdate(employee)

    db.run(query)
  }

  /*
  * Retrieving all the records in the Table
  * */

  def getAll: Future[List[Employee]] = db.run {
    employeeTableQuery.to[List].result
  }

  /*
  * Updating multiple fields in the record with the given id
  * */

  def updateMultiple(id: Int, newValues: (String, Double)): Future[Int] = {
    val query = employeeTableQuery.filter(_.id === id)
      .map(record => (record.name, record.experience)).update(newValues)
    db.run(query)
  }

/*
  *using join on dependents and employee
  * */
  def namesOfDependentWithEmployees: Future[List[(String, String)]] = {
    val action = {
      for {
        (emp, dep) <- employeeTableQuery join dependentTableQuery on (_.id === _.empId)
      } yield (emp.name, dep.name)
    }.to[List].result
    db.run(action)
  }

 /*
 *Inserting record using "Plain Sql"
 * */
  def insertWithPlainSql: Future[Int] = {
    val action = sqlu"insert into employee values(102, 'anmol', 5);"
    db.run(action)

  }

}

object EmployeeRepo extends EmployeeRepo  with PostgresComponent



