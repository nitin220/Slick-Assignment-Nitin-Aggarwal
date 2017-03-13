package com.knoldus

import scala.concurrent.ExecutionContext.Implicits.global
import com.knoldus.model.{Dependent, Project, Employee}
import com.knoldus.repo.{DependentRepo, ProjectRepo, EmployeeRepo}

object Tester {
  def main(args: Array[String]): Unit = {
    /*val obj=new EmployeeRepo

    //obj.create
    //Thread.sleep(10000)

    val insertRes = obj.insert(Employee(3, "Ankit", 7.0))
    val res = insertRes.map { res => s"$res row inserted" }.recover {
      case ex: Throwable => ex.getMessage
    }

    res.map(println(_))

    /*obj.deleteOnBasisOfName("Akash")*/

    /*val upsertRecord = obj.upsert(Employee(15,"Akash",3.0))
    val upsertResult = upsertRecord.map { upsertResult => s"$upsertResult row updated/inserted" }.recover{
      case ex: Throwable => s"Upsert Failed ${ex.getMessage}"
    }

    upsertResult.map(println(_))*/

    val res3=obj.updateTuple(3,("Ankit Kumar BArthwal",6.0))

    val res4 = insertRes.map { res => s"$res row inserted" }.recover {
      case ex: Throwable => ex.getMessage
    }

    res4.map(println(_))

    val all=obj.getAll
    val res1=all.map{ res=>res.map(println(_))}.recover{
      case ex: Throwable => ex.getMessage
    }*/

    /*val obj1=new ProjectRepo

    /*obj1.create*/

    /*val insertRes = obj1.insert(Project(2, "KarbonData", 1, 10, "Himanshu"))
    val res = insertRes.map { res => s"$res row inserted" }.recover {
      case ex: Throwable => ex.getMessage
    }

    res.map(println(_))
*/
    /*obj1.deleteOnBasisOfId(1)*/

    obj1.getProjectWithEmployee.map(c=>c.map(println(_))).recover {
      case ex: Throwable => ex.getMessage
    }

    obj1.getAllProjectForGivenEmployee(1).map(c=>c.map(println(_))).recover {
      case ex: Throwable => ex.getMessage
    }*/

    val obj3=new DependentRepo
    /*obj3.create*/
    val insertRes1 = obj3.insert(Dependent(2, 1, "Rohit", "brother", None)).map { res => s"$res row inserted" }.recover {
      case ex: Throwable => ex.getMessage
    }

    insertRes1.map(println(_))

    /*obj3.delete(1)*/

    obj3.getAll.map{ res=>res.map(println(_))}.recover{
      case ex: Throwable => ex.getMessage
    }


    Thread.sleep(10000)

  }
}
