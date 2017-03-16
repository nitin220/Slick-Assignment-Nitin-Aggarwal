package repo


import com.knoldus.model.Employee
import com.knoldus.repo.EmployeeRepo
import connection.H2Component
import org.scalatest.AsyncFunSuite


object testObject extends EmployeeRepo with H2Component

class EmployeeRepoTest extends AsyncFunSuite {



  test("Should get all data"){
    testObject.getAll.map(res=>assert(res==List(Employee(1, "Nitin", 5.0),Employee(2, "Anmol", 7.0),
      Employee(3,"Amita",9.0))))
  }

  test("Should insert a row"){
    testObject.insert(Employee(6, "Amita", 9.0)).map(res=>assert(res==1))
  }

  /*test("Should insert a row and return inserted row"){
    testObject.insertReturningObject(Employee(6, "Amita", 9.0)).map(res=>assert(res==Employee(6, "Amita", 9.0)))
  }*/
  test("Should insert multiple row"){
    testObject.insertMultiple(Employee(4, "Jatin", 9.0),Employee(5, "Mahesh", 9.0)).map(res=>assert(res==1))
  }

  test("Should delete a record by experience"){
    testObject.deleteOnBasisOfExperience(9.0).map(res=>assert(res==1))
  }

  test("Should delete a record by id"){
    testObject.deleteOnBasisOfId(3).map(res=>assert(res==1))
  }

  test("Should delete a record by name"){
    testObject.deleteOnBasisOfName("Amita").map(res=>assert(res==1))
  }

  test("Should update a record's name"){
    testObject.updateName(1,"Nitin Aggarwal").map(res=>assert(res==1))
  }

  test("Should update a record's experience"){
    testObject.updateExperience(1,8.0).map(res=>assert(res==1))
  }

  test("Should update a record or insert a record in case record is not there in database i.e. Upsert"){
    testObject.upsert(Employee(3,"Amita Rajput",9.0)).map(res=>assert(res==1))
  }

  test("Should update mutiple fields of a record"){
    testObject.updateMultiple(3,("Amita Rajput",9.0)).map(res=>assert(res==1))
  }

  test("Should apply join and generate List of all employees who has dependents"){
    testObject.namesOfDependentWithEmployees.map(res=>assert(res==List(("Anmol","Pushpa"),("Nitin","Mohil"))))
  }

  test("Should insert a row through plain sql"){
    testObject.insertWithPlainSql.map(res=>assert(res==1))
  }

}
