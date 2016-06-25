package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.Future
import models.Tables.{FightsRow, Fights, Boxers, BoxersRow, BoxingTypes}
import java.util.Date
import scala.concurrent.ExecutionContext.Implicits.global

//case class FightWithBoxers(fight: FightsRow, boxer: BoxersRow)

case class FightWithBoxers(fight: FightsRow, firstBoxer: BoxersRow, secondBoxer: BoxersRow)

class FightRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import dbConfig.driver.api._
    
  def all = {
    val tupledJoin: Query[(Fights,Boxers,Boxers),(FightsRow,BoxersRow,BoxersRow), Seq] = for {
        f <- Fights
        b1 <- Boxers if f.firstBoxerId === b1.id
        b2 <- Boxers if f.secondBoxerId === b2.id
    } yield (f, b1, b2)
    
    db.run(tupledJoin.to[List].result).map(_.map(FightWithBoxers.tupled))
  }
    
  def findByMinimumDate(date: Date): Future[List[FightsRow]] =
    db.run(Fights.filter(_.date >= new java.sql.Date(date.getTime)).to[List].result)
    
  def findById(id: Long) = {
    val tupledJoin: Query[(Fights,Boxers,Boxers),(FightsRow,BoxersRow,BoxersRow), Seq] = for {
      f <- Fights
      b1 <- Boxers if f.firstBoxerId === b1.id
      b2 <- Boxers if f.secondBoxerId === b2.id
    } yield (f, b1, b2)
    
    db.run(tupledJoin.filter(_._1.id === id).result.headOption).map(_.map(FightWithBoxers.tupled))
  }
}