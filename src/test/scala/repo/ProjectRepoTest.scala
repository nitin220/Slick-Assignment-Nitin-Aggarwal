package repo

import com.knoldus.model.Project
import com.knoldus.repo.ProjectRepo
import connection.H2Component
import org.scalatest.AsyncFunSuite

object testObject2 extends ProjectRepo with H2Component

class ProjectRepoTest extends AsyncFunSuite{

  test("Should get all data"){
    testObject2.getAll.map(res=>assert(res==List(Project(1, "coarbondata", 2,10,"Pankhuri")
      ,Project(2, "Fit files", 1,10,"Mehta"))))
  }

  test("Should insert a row"){
    testObject2.insert(Project(3, "Fit files", 3,10,"Mehta")).map(res=>assert(res==1))
  }

  test("Should insert Multiple row"){
    testObject2.insertMultiple(Project(3, "Fit files", 3,10,"Mehta"),
      Project(4, "Fit files", 3,10,"Mehta")).map(res=>assert(res==List(1,1)))
  }

  test("Should delete on the basis of Id"){
    testObject2.deleteOnBasisOfId(2).map(res=>assert(res==1))
  }

  test("Should delete on the basis of name"){
    testObject2.deleteOnBasisOfName("Fit files").map(res=>assert(res==1))
  }

  test("Should update name field in record"){
    testObject2.updateName(2,"carbondata").map(res=>assert(res==1))
  }

  test("Should update multiple fields in record"){
    testObject2.updateMultiple(2,("carbondata",1,11,"Prashant")).map(res=>assert(res==1))
  }

  test("Should update a record or insert a record in case record is not there in database i.e. Upsert"){
    testObject2.upsert( Project(2, "carbondata", 1,10,"Mehta")).map(res=>assert(res==1))
  }

  test("should generate List of all project name with its employee name "){
    testObject2.getProjectWithEmployee.map(res=>assert(res==List(("Anmol","coarbondata"),("Nitin","Fit files"))))
  }

  test("should generate List of all project name for given employee id"){
    testObject2.getAllProjectForGivenEmployee(1).map(res=>assert(res==List("Fit files")))
  }

  test("Should apply join and generate List of all project name with its employee name "){
    testObject2.getProjectWithEmployeeNames.map(res=>assert(res==List(("coarbondata","Anmol"),("Fit files","Nitin"))))
  }

  test("Should insert a row using plain sql"){
    testObject2.insertWithPlainSql.map(res=>assert(res==1))
  }



}
