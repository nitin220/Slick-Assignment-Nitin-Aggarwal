package com.knoldus.repo

import com.knoldus.connection.{DBComponent, PostgresComponent}
import com.knoldus.mapping.DependentTable
import com.knoldus.model.Dependent

import scala.concurrent.Future


class DependentRepo extends DependentTable with PostgresComponent {

  this: DBComponent =>

  import driver.api._

  /*
  * creating schema for the given table
  * */

  def create: Future[Unit] = db.run(dependentTableQuery.schema.create)

  /*
  * inserting a record
  * */

  def insert(dependent: Dependent): Future[Int] = db.run {
    dependentTableQuery += dependent
  }

  /*
  * deleting a record
  * */

  def delete(empid: Int): Future[Int] = {
    val query = dependentTableQuery.filter(_.empId === empid)
    val action = query.delete
    db.run(action)
  }

  /*
  * Updating the name field of the record for the given id
  * */

  def updateName(id: Int, name: String): Future[Int] = {
    val query = dependentTableQuery.filter(_.id === id).map(_.name).update(name)
    db.run(query)
  }

  /*
  * Updating multiple fields of record for the given id
  * */

  def updateMultiple(id: Int, newValues: (String, Int, String, Option[Int])): Future[Int] = {
    val query = dependentTableQuery.filter(_.id === id)
      .map(record => (record.name, record.empId, record.relation, record.age)).update(newValues)
    db.run(query)
  }

  /*
  * Insertion(if record is not present) or updation(if existing record) ie upsert
  * */

  def upsert(dependent: Dependent): Future[Int] = {
    val query = dependentTableQuery.insertOrUpdate(dependent)
    dependentTableQuery += dependent
    db.run(query)
  }

  /*
  * Retrieving all the records in the Table
  * */

  def getAll: Future[List[Dependent]] = db.run {
    dependentTableQuery.to[List].result
  }

  /*
  * Retrieving the dependent details associated with the given Employee id
  * */

  def getAllDependentForGivenEmployee(employeeId: Int): Future[List[Dependent]] = db.run {
    dependentTableQuery.filter(_.empId === employeeId).to[List].result
  }


}
