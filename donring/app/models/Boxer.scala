package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider

import slick.driver.JdbcProfile
import scala.concurrent.Future
import models.Tables.BoxersRow
import models.Tables.Boxers

class BoxerRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  val db = dbConfig.db
    
  def all: Future[List[BoxersRow]] =
    db.run(Boxers.to[List].result)
    
  def findByName(firstName: String, lastName: String): Future[Option[BoxersRow]] = {
    db.run(Boxers.filter(boxer => (boxer.firstName === firstName && boxer.lastName === lastName)).result.headOption)
  }
    
    /*db.run(_findById(id))
    Projects.filter(_.id === id).result.headOption*/
}