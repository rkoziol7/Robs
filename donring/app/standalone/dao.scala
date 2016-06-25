package standalone

import slick.driver.MySQLDriver.api._
import scala.concurrent.Future
import models.Tables.BoxersRow
import models.Tables.Boxers
import models.Tables.Fights
import models.Tables.FightsRow
import models.Tables.BoxingTypesRow
import models.Tables.BoxingTypes
import java.util.Calendar
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import models.Tables.BookiesOddsRow
import models.Tables.BookiesOdds
import models.Tables.Bookies

trait DBDao {
  val db = Database.forConfig("slick.dbs.default.db")
}

object BoxerDao extends DBDao {
  def findByName(firstName: String, lastName: String): Future[Option[BoxersRow]] = {
    //println("boxerDao", firstName, lastName, firstBoxer)
    db.run(Boxers.filter(boxer => boxer.firstName === firstName && boxer.lastName === lastName).result.headOption)
    //val r = db.run(Boxers.filter(boxer => (boxer.firstName === firstName && boxer.lastName === lastName)).result)
    //return r
  }
}

/*class FightOperations {
  val db: Option[MySQLDriverBack] = None
  
  def createFight(firstBoxer: BoxersRow, secondBoxer: BoxersRow, eventDate: java.sql.Date): Future[Long] = {
    //case class FightsRow(id: Int, firstBoxerId: Int, secondBoxerId: Int, date: java.sql.Date, place: Option[String] = None, resultTypeId: Option[Int] = None, winner: Option[Int] = None, datetimeCreated: java.sql.Timestamp)
    //val fight = FightsRow(0, firstBoxer.id, secondBoxer.id, date, new java.sql.Timestamp(0))
    
    val fight = FightsRow(id = 0, firstBoxerId = firstBoxer.id, secondBoxerId = secondBoxer.id, date = eventDate, datetimeCreated = new java.sql.Timestamp(0))
    
    db match{
      case Some(s) => s.run(Fights returning Fights.map(_.id) += fight)
      case None    => {
        println("Database was not initialized!")
        Future(0.toLong)
      }
    }
    //db.run(Fights returning Fights.map(_.id) += fight)
  }
}*/

object BookiesDao extends DBDao {
  def findByName(bookieName : String) =
    db.run(Bookies.filter(bookie => bookie.name === bookieName).result.headOption)
    
  def createOdds(fightId: Long, firstWin: Float, draw: Option[Float], secondWin: Float, bookieId: Option[Int], bookieName: Option[String]): Future[Long] = {
    var bId = bookieId.getOrElse(0)
    
    bookieName match {
      case Some(name) => {
        val id = for {
          bookie <- findByName(name) map {_.getOrElse(throw new NoBoxerException("No bookie " + bookieName + " in database!"))}
        } yield bookie.id
        
        id onSuccess {
          case id => bId = id
        }
      }
      case None => {}
    }
    
    //val timestamp = new java.sql.Timestamp(new java.util.Date().getTime)
    val odds = BookiesOddsRow(0, fightId, firstWin, draw, secondWin, bId, None)
    db.run(BookiesOdds returning BookiesOdds.map(_.id) += odds)
  }
}

object FightDao extends DBDao {
  def createFight(firstBoxer: BoxersRow, secondBoxer: BoxersRow, eventDate: java.sql.Date): Future[Long] = {
    val fight = FightsRow(id = 0, firstBoxerId = firstBoxer.id, secondBoxerId = secondBoxer.id, date = eventDate, datetimeCreated = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))
    //db.run(Projects returning Projects.map(_.id) += project)
    Logger.info("Creating a new fight between " + firstBoxer + " and " + secondBoxer)
    db.run(Fights returning Fights.map(_.id) += fight)
  }
}