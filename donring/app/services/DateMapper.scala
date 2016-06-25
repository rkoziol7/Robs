package services

import slick.driver.MySQLDriver.api._

object DateMapper {
  val utilDate2SqlTimestampMapper = MappedColumnType.base[java.util.Date, java.sql.Timestamp] (
    { utilDate => new java.sql.Timestamp(utilDate.getTime()) },
    { sqlTimestamp => new java.util.Date(sqlTimestamp.getTime()) }
  )

  val utilDate2SqlDate = MappedColumnType.base[java.util.Date, java.sql.Date] (
    { utilDate => new java.sql.Date(utilDate.getTime()) },
    { sqlDate => new java.util.Date(sqlDate.getTime()) }
  )
}