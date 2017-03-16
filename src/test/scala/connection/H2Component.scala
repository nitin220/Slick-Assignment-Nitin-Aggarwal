package connection


import java.util.UUID

import com.knoldus.connection.DBComponent
import slick.jdbc.H2Profile

trait H2Component extends DBComponent {

  val driver = H2Profile

  import driver.api.Database

  //  val db: Database = Database.forConfig("myH2DB")

  val randomDB = "jdbc:h2:mem:test" +
    UUID.randomUUID().toString + ";"
  val h2Url = randomDB + "MODE=PostgreSQL;DATABASE_TO_UPPER=false;" + "INIT=RUNSCRIPT FROM 'src/test/resources/schema.sql'\\;RUNSCRIPT FROM 'src/test/resources/testContent.sql'"
  println(s"\n\n~~~~~~~~~~~~~~~~~~~~~             $h2Url         ~~~~~~~~~~~~~~~~~~~~~~~\n\n")
  val db: Database = Database.forURL(url = h2Url, driver = "org.h2.Driver")

}

