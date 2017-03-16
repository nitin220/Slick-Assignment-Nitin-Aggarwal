package com.knoldus.repo

import com.knoldus.connection.{DBComponent, PostgresComponent}
import com.knoldus.mapping.{EmployeeTable, DependentTable}
import com.knoldus.model.Dependent

import scala.concurrent.Future


trait DependentRepo extends DependentTable with EmployeeTable{

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
  * inserting a record and returning Id of inserted record
  * */

  def insertReturningId(newDependent: Dependent): Future[Int] =  {
    val action=dependentTableAutoInc += newDependent
    db.run(action)
  }

  /*
  * inserting a record and returning inserted record
  * */

  def insertReturningObject(newDependent: Dependent): Future[Dependent] =  {
    val action=dependentTableObject += newDependent
    db.run(action)
  }

  /*
 * inserting multiple records in sequence
 * */

  def insertMultiple(newDependent: Dependent,newDependent2:Dependent): Future[Int] = {
    val action1:DBIO[Int]=dependentTableQuery += newDependent
    val action2:DBIO[Int]=dependentTableQuery +=newDependent2
    //val a4: DBIO[List[Int]] = DBIO.sequence(List(action1, action2)).cleanUp(x=>action1)
    val query=action1.andThen(action2).transactionally
    //db.run(a4)
    db.run(query)
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
    db.run(query)
  }

  /*
  * Retrieving all the records in the Table
  * */

  def getAll: Future[List[Dependent]] = db.run {
    dependentTableQuery.to[List].result
  }

  /*
  * Retrieving the dependent name associated with the given Employee id
  * */

  def getAllDependentForGivenEmployee(employeeId: Int): Future[List[String]] = db.run {
    dependentTableQuery.filter(_.empId === employeeId).map(_.name).to[List].result
  }

  /*
  *using join on dependents and employee
  * */
  def namesOfDependentWithEmployees: Future[List[(String, String)]] = {
    val action = {
      for {
        (dep,emp) <- dependentTableQuery join employeeTableQuery on (_.empId === _.id)
      } yield (dep.name, emp.name)
    }.to[List].result
    db.run(action)
  }

  /*
  *Inserting record using "Plain Sql"
  * */
  def insertWithPlainSql: Future[Int] = {
    val action = sqlu"insert into dependent values(8,1,'Sahil','Brother','24');"
    db.run(action)

  }


}

object DependentRepo extends PostgresComponent
