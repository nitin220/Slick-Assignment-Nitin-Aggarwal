package com.knoldus.repo

import com.knoldus.connection.{MySqlComponent, DBComponent, PostgresComponent}
import com.knoldus.mapping.{EmployeeTable, ProjectTable}
import com.knoldus.model.{Employee, Project}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait ProjectRepo extends ProjectTable with EmployeeTable {

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
  * inserting a record and returning Id of inserted record
  * */

  def insertReturningId(newProject: Project): Future[Int] =  {
    val action=projectTableAutoInc += newProject
    db.run(action)
  }

  /*
  * inserting a record and returning inserted record
  * */

  def insertReturningObject(newProject: Project): Future[Project] =  {
    val action=projectTableObject +=newProject
    db.run(action)
  }

  /*
  * inserting multiple records in sequence
  * */

  def insertMultiple(proj: Project, proj2: Project): Future[List[Int]] = {
    val action1: DBIO[Int] = projectTableQuery += proj
    val action2: DBIO[Int] = projectTableQuery += proj2
   /* val a4: DBIO[List[Int]] = DBIO.sequence(List(action1, action2)).cleanUp{
      case Some(_)=>projectTableQuery.schema.truncate
      case None => projectTableQuery.to[List].result
    }*/
    val query: DBIO[List[Int]] = DBIO.sequence(List(action1, action2)).transactionally
    db.run(query)
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
    db.run(query)
  }

  /*
  * Retrieving all the records in the Table
  * */

  def getAll: Future[List[Project]] = db.run {
    projectTableQuery.to[List].result
  }

  /*
  * Retrieving the Project details along with Employee name
  * */

  def getProjectWithEmployee: Future[List[(String, String)]] = db.run {
    (for {
      record <- projectTableQuery
      employee <- record.employeeProjectFK
    } yield (employee.name, record.name)).to[List].result
  }

  /*
  * Retrieving the Project name associated with the given Employee id
  * */

  def getAllProjectForGivenEmployee(employeeId: Int): Future[List[String]] = db.run {
    projectTableQuery.filter(_.empId === employeeId).map(_.name).to[List].result
  }

  /*
  *using join on project and employee
  * */

  def getProjectWithEmployeeNames: Future[List[(String, String)]] = {
    val action = {
      for {
        (proj,emp) <- projectTableQuery join employeeTableQuery on (_.empId === _.id)
      } yield (proj.name, emp.name)
    }.to[List].result
    db.run(action)
  }

  /*
 *Inserting record using "Plain Sql"
 * */
  def insertWithPlainSql: Future[Int] = {
    val action = sqlu"insert into project values(10,'carbondata',2,10,'anmol');"
    db.run(action)

  }

}

object ProjectRepo extends PostgresComponent
