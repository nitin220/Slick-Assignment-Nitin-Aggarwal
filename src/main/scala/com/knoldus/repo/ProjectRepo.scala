package com.knoldus.repo

import com.knoldus.connection.{DBComponent, PostgresComponent}
import com.knoldus.mapping.ProjectTable
import com.knoldus.model.{Employee, Project}

import scala.concurrent.Future


class ProjectRepo extends ProjectTable with PostgresComponent {

  this: DBComponent =>

  import driver.api._

  /*
  * creating schema for the given table
  * */

  def create: Future[Unit] = db.run(projectTableQuery.schema.create)

  /*
  * inserting a record
  * */

  def insert(proj: Project): Future[Int] = db.run {
    projectTableQuery += proj
  }

  /*
 * deleting a record on the basis of project id
 * */

  def deleteOnBasisOfId(id: Int): Future[Int] = {
    val query = projectTableQuery.filter(_.projId === id).delete
    db.run(query)
  }

  /*
 * deleting a record on the basis of project name
 * */

  def deleteOnBasisOfName(name: String): Future[Int] = {
    val query = projectTableQuery.filter(_.name === name).delete
    db.run(query)
  }

  /*
  * Updating the name field of the record for the given id
  * */

  def updateName(id: Int, name: String): Future[Int] = {
    val query = projectTableQuery.filter(_.projId === id).map(_.name).update(name)
    db.run(query)
  }

  /*
  * Updating multiple fields of record for the given id
  * */

  def updateMultiple(id: Int, newValues: (String, Int, Int, String)): Future[Int] = {
    val query = projectTableQuery.filter(_.projId === id)
      .map(record => (record.name, record.empId, record.teamSize, record.teamLead)).update(newValues)
    db.run(query)
  }

  /*
  * Insertion(if record is not present) or updation(if existing record) ie uppsert
  * */

  def upsert(project: Project): Future[Int] = {
    val query = projectTableQuery.insertOrUpdate(project)
    projectTableQuery += project
    db.run(query)
  }

  /*
  * Retrieving all the records in the Table
  * */

  def getAll: Future[List[Project]] = db.run {
    projectTableQuery.to[List].result
  }

  /*
  * Retrieving the Project details along with Employee details
  * */

  def getProjectWithEmployee: Future[List[(Employee, Project)]] = db.run {
    (for {
      record <- projectTableQuery
      employee <- record.employeeProjectFK
    } yield (employee, record)).to[List].result
  }

  /*
  * Retrieving the Project details associated with the given Employee id
  * */

  def getAllProjectForGivenEmployee(employeeId: Int): Future[List[Project]] = db.run {
    projectTableQuery.filter(_.empId === employeeId).to[List].result
  }

}
