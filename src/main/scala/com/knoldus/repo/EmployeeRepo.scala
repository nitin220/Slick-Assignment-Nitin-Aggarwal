package com.knoldus.repo

import com.knoldus.connection.{MySqlComponent, PostgresComponent, DBComponent}
import com.knoldus.mapping.EmployeeTable
import com.knoldus.model.Employee

import scala.concurrent.Future


class EmployeeRepo extends EmployeeTable with MySqlComponent {

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
    employeeTableQuery += employee
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


}
