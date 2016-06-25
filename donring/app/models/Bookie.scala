package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.Future
import models.Tables.BookiesRow
import models.Tables.Bookies

class BookieRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import dbConfig.driver.api._
    
  def all: Future[List[BookiesRow]] =
    db.run(Bookies.to[List].result)
}