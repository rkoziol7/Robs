package standalone

import com.typesafe.config.ConfigFactory
import Utils.downloadFileCache
import java.util.Date
import java.text.SimpleDateFormat
import models.BoxerRepo
import play.api.db.slick.DatabaseConfigProvider
import javax.inject.Inject
import slick.backend.DatabasePublisher
import slick.driver.MySQLDriver.api._
import slick.backend.DatabaseConfig
import slick.profile.BasicProfile
import play.api.Play
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Logger
import scala.util.{Success, Failure}
import scala.concurrent.Future
import models.Tables.BoxersRow

case class NoBoxerException(message: String) extends Exception(message) {}

case class Boxer(val firstName: String, val lastName: String) {
  def fullName(): String = {
    return firstName + " " + lastName
  }
}
case class BoxingEvent(val date: Date, val weightClass: String, val live: Boolean, val firstBoxer: Boxer, val secondBoxer: Boxer, val periods: Seq[Period]) { }
case class Period(val description: String, val firstOdds: Option[Float], val drawOdds: Option[Float], val secondOdds: Option[Float]) {}

object PinnacleParser extends Parser with App {
    def handleNoBoxerException(noBoxer: Exception) {
      println("handleNoBoxerException")
      Logger.warn(noBoxer.getMessage);
    }
  
    val boxerDao = BoxerDao
    val fightDao = FightDao
    val bookiesDao = BookiesDao
    
    val now = new Date
    //val emptyBoxersRow = new BoxersRow(id=0, birthDate=new java.sql.Date(now.getTime), lastUpdated=new java.sql.Timestamp(now.getTime))
  
  //def parseBookieFile: Unit = {
    //val boxerRepo = new BoxerRepo(testDbProvider)
  
    val url = "http://xml.pinnaclesports.com/pinnacleFeed.aspx?sportType=Boxing"
    val timeoutSecs = 36000
    //val emptyBoxer = Boxer("NONAME", "NONAME")
    
    val cachePath = ConfigFactory.load().getString("donring.cache.path");
    
    val pinnacleFilePath = downloadFileCache(url, cachePath, timeoutSecs)
    
    val pinnacleElem = scala.xml.XML.loadFile(pinnacleFilePath)
    
    Logger.info("Starting the pinnacle script")
   
    var boxingEvents = (pinnacleElem \ "events" \ "event").map { event =>
      val participants = (event \ "participants" \ "participant").map { participant =>
        Boxer((participant \ "participant_name").text.split(" ").lift(0).getOrElse("NONAME"), (participant \ "participant_name").text.split(" ").lift(1).getOrElse("NONAME"))
      }
      BoxingEvent(
        new SimpleDateFormat("yyyy-MM-dd HH:mm").parse((event \ "event_datetimeGMT").text),
        (event \ "league").text,
        if ((event \ "IsLive").text == "No") false else true,
        participants(0),
        participants(1),
        (event \ "periods" \ "period").map { period =>
          Period((period \ "period_update").text, convertAmericanToEuropean(americanOddsString = (period \ "moneyline" \ "moneyline_visiting").text), Some(0), convertAmericanToEuropean(americanOddsString = (period \ "moneyline" \ "moneyline_home").text))
        }
        //(event \ "periods" \ "period" \ "moneyline" \ "moneyline_visiting")
      )
    } filter(event => event.live == false)
    
    //for (event <- boxingEvents) {
    boxingEvents.foreach { event =>
      //event.periods.filter(period => period.description == "open")
      
      var fId = for {
        
        firstBoxer <- boxerDao.findByName(event.firstBoxer.firstName, event.firstBoxer.lastName) map {_.getOrElse(throw new NoBoxerException("No boxer " + event.firstBoxer.firstName + " " + event.firstBoxer.lastName + " in database!"))}
        secondBoxer <- boxerDao.findByName(event.secondBoxer.firstName, event.secondBoxer.lastName) map {_.getOrElse(throw new NoBoxerException("No boxer " + event.firstBoxer.firstName + " " + event.firstBoxer.lastName + " in database!"))}
        
        //Logger.info("Creating a new fight")
        fightId <- fightDao.createFight(firstBoxer, secondBoxer, new java.sql.Date(event.date.getTime)) map { x => x }
        //oddsId <- bookiesDao.createOdds(fightId, event.periods.head.firstOdds.getOrElse(0), event.periods.head.drawOdds, event.periods.head.secondOdds.getOrElse(0), None, bookieName = Some("pinnacle"))
      } yield fightId

      //Thread sleep 1000 //TODO: remove that
      
      fId onComplete {
        case Success(id) => Logger.info("Great success: " + id)
        case Failure(t) => Logger.warn("An error has occured: " + t.getMessage)
      }
    }
}