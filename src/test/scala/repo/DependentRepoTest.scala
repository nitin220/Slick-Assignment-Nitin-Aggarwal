package repo

import com.knoldus.model.Dependent
import com.knoldus.repo.DependentRepo
import connection.H2Component
import org.scalatest.AsyncFunSuite

object testObject3 extends DependentRepo with H2Component

class DependentRepoTest extends AsyncFunSuite{

  test("Should get all data"){
    testObject3.getAll.map(res=>assert(res==List(Dependent(1, 2,"Pushpa","sister",Some(23)),
      Dependent(2, 1,"Mohil","Brother",Some(24)))))
  }

  test("Should insert a row"){
    testObject3.insert(Dependent(3, 1,"Neha","Sister",Some(24))).map(res=>assert(res==1))
  }

  test("Should insert multiple row"){
    testObject3.insertMultiple(Dependent(3, 1,"Neha","Sister",Some(24)),
      Dependent(4, 2,"Pinky","Sister",Some(24))).map(res=>assert(res==1))
  }

  test("Should delete a row"){
    testObject3.delete(2).map(res=>assert(res==1))
  }

  test("Should update a row"){
    testObject3.updateName(1,"Peter").map(res=>assert(res==1))
  }

  test("Should update multiple row"){
    testObject3.updateMultiple(1,("Peter",2,"Brother",Some(3))).map(res=>assert(res==1))
  }

  test("Should update a record or insert a record in case record is not there in database i.e. Upsert"){
    testObject3.upsert(Dependent(3, 1,"Neha","Sister",Some(24))).map(res=>assert(res==1))
  }

  test("ShouldGet all dependent by employee id"){
    testObject3.getAllDependentForGivenEmployee(1).map(res=>assert(res==List("Mohil")))
  }

  test("Should apply join and generate List of all employees who has dependents"){
    testObject3.namesOfDependentWithEmployees.map(res=>assert(res==List(("Pushpa","Anmol"),("Mohil","Nitin"))))
  }

  test("Should insert a row using plain sql"){
    testObject3.insertWithPlainSql.map(res=>assert(res==1))
  }

}
