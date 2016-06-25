package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Bookies.schema, BookiesOdds.schema, Boxers.schema, BoxingTypes.schema, Fights.schema, ResultTypes.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Bookies
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(50,true) */
  case class BookiesRow(id: Int, name: String)
  /** GetResult implicit for fetching BookiesRow objects using plain SQL queries */
  implicit def GetResultBookiesRow(implicit e0: GR[Int], e1: GR[String]): GR[BookiesRow] = GR{
    prs => import prs._
    BookiesRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table bookies. Objects of this class serve as prototypes for rows in queries. */
  class Bookies(_tableTag: Tag) extends Table[BookiesRow](_tableTag, "bookies") {
    def * = (id, name) <> (BookiesRow.tupled, BookiesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> BookiesRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(50,true) */
    val name: Rep[String] = column[String]("name", O.Length(50,varying=true))

    /** Uniqueness Index over (name) (database name name) */
    val index1 = index("name", name, unique=true)
  }
  /** Collection-like TableQuery object for table Bookies */
  lazy val Bookies = new TableQuery(tag => new Bookies(tag))

  /** Entity class storing rows of table BookiesOdds
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fightId Database column fight_id SqlType(BIGINT), Default(0)
   *  @param firstWin Database column first_win SqlType(FLOAT), Default(1.0)
   *  @param draw Database column draw SqlType(FLOAT), Default(None)
   *  @param secondWin Database column second_win SqlType(FLOAT), Default(1.0)
   *  @param bookieId Database column bookie_id SqlType(INT)
   *  @param timestamp Database column timestamp SqlType(TIMESTAMP) */
  case class BookiesOddsRow(id: Long, fightId: Long = 0L, firstWin: Float = 1.0F, draw: Option[Float] = None, secondWin: Float = 1.0F, bookieId: Int, timestamp: Option[java.sql.Timestamp])
  /** GetResult implicit for fetching BookiesOddsRow objects using plain SQL queries */
  implicit def GetResultBookiesOddsRow(implicit e0: GR[Long], e1: GR[Float], e2: GR[Option[Float]], e3: GR[Int], e4: GR[Option[java.sql.Timestamp]]): GR[BookiesOddsRow] = GR{
    prs => import prs._
    BookiesOddsRow.tupled((<<[Long], <<[Long], <<[Float], <<?[Float], <<[Float], <<[Int], <<?[java.sql.Timestamp]))
  }
  /** Table description of table bookies_odds. Objects of this class serve as prototypes for rows in queries. */
  class BookiesOdds(_tableTag: Tag) extends Table[BookiesOddsRow](_tableTag, "bookies_odds") {
    def * = (id, fightId, firstWin, draw, secondWin, bookieId, timestamp) <> (BookiesOddsRow.tupled, BookiesOddsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(fightId), Rep.Some(firstWin), draw, Rep.Some(secondWin), Rep.Some(bookieId), timestamp).shaped.<>({r=>import r._; _1.map(_=> BookiesOddsRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column fight_id SqlType(BIGINT), Default(0) */
    val fightId: Rep[Long] = column[Long]("fight_id", O.Default(0L))
    /** Database column first_win SqlType(FLOAT), Default(1.0) */
    val firstWin: Rep[Float] = column[Float]("first_win", O.Default(1.0F))
    /** Database column draw SqlType(FLOAT), Default(None) */
    val draw: Rep[Option[Float]] = column[Option[Float]]("draw", O.Default(None))
    /** Database column second_win SqlType(FLOAT), Default(1.0) */
    val secondWin: Rep[Float] = column[Float]("second_win", O.Default(1.0F))
    /** Database column bookie_id SqlType(INT) */
    val bookieId: Rep[Int] = column[Int]("bookie_id")
    /** Database column timestamp SqlType(TIMESTAMP) */
    val timestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("timestamp")

    /** Foreign key referencing Bookies (database name FK_bookies_odds_bookies) */
    lazy val bookiesFk = foreignKey("FK_bookies_odds_bookies", bookieId, Bookies)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Fights (database name FK_bookies_odds_fights) */
    lazy val fightsFk = foreignKey("FK_bookies_odds_fights", fightId, Fights)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (fightId,bookieId,timestamp) (database name fight_id) */
    val index1 = index("fight_id", (fightId, bookieId, timestamp), unique=true)
  }
  /** Collection-like TableQuery object for table BookiesOdds */
  lazy val BookiesOdds = new TableQuery(tag => new BookiesOdds(tag))

  /** Entity class storing rows of table Boxers
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param firstName Database column first_name SqlType(VARCHAR), Length(50,true), Default(0)
   *  @param lastName Database column last_name SqlType(VARCHAR), Length(50,true), Default(0)
   *  @param nick Database column nick SqlType(VARCHAR), Length(50,true), Default(None)
   *  @param boxingTypeId Database column boxing_type_id SqlType(INT), Default(0)
   *  @param birthDate Database column birth_date SqlType(DATE)
   *  @param boxrecAddress Database column boxrec_address SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param lastUpdated Database column last_updated SqlType(TIMESTAMP) */
  case class BoxersRow(id: Int, firstName: String = "0", lastName: String = "0", nick: Option[String] = None, boxingTypeId: Int = 0, birthDate: java.sql.Date, boxrecAddress: Option[String] = None, lastUpdated: java.sql.Timestamp)
  /** GetResult implicit for fetching BoxersRow objects using plain SQL queries */
  implicit def GetResultBoxersRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Date], e4: GR[java.sql.Timestamp]): GR[BoxersRow] = GR{
    prs => import prs._
    BoxersRow.tupled((<<[Int], <<[String], <<[String], <<?[String], <<[Int], <<[java.sql.Date], <<?[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table boxers. Objects of this class serve as prototypes for rows in queries. */
  class Boxers(_tableTag: Tag) extends Table[BoxersRow](_tableTag, "boxers") {
    def * = (id, firstName, lastName, nick, boxingTypeId, birthDate, boxrecAddress, lastUpdated) <> (BoxersRow.tupled, BoxersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(firstName), Rep.Some(lastName), nick, Rep.Some(boxingTypeId), Rep.Some(birthDate), boxrecAddress, Rep.Some(lastUpdated)).shaped.<>({r=>import r._; _1.map(_=> BoxersRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column first_name SqlType(VARCHAR), Length(50,true), Default(0) */
    val firstName: Rep[String] = column[String]("first_name", O.Length(50,varying=true), O.Default("0"))
    /** Database column last_name SqlType(VARCHAR), Length(50,true), Default(0) */
    val lastName: Rep[String] = column[String]("last_name", O.Length(50,varying=true), O.Default("0"))
    /** Database column nick SqlType(VARCHAR), Length(50,true), Default(None) */
    val nick: Rep[Option[String]] = column[Option[String]]("nick", O.Length(50,varying=true), O.Default(None))
    /** Database column boxing_type_id SqlType(INT), Default(0) */
    val boxingTypeId: Rep[Int] = column[Int]("boxing_type_id", O.Default(0))
    /** Database column birth_date SqlType(DATE) */
    val birthDate: Rep[java.sql.Date] = column[java.sql.Date]("birth_date")
    /** Database column boxrec_address SqlType(VARCHAR), Length(255,true), Default(None) */
    val boxrecAddress: Rep[Option[String]] = column[Option[String]]("boxrec_address", O.Length(255,varying=true), O.Default(None))
    /** Database column last_updated SqlType(TIMESTAMP) */
    val lastUpdated: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("last_updated")

    /** Foreign key referencing BoxingTypes (database name FK__boxing_types) */
    lazy val boxingTypesFk = foreignKey("FK__boxing_types", boxingTypeId, BoxingTypes)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (firstName,lastName,birthDate) (database name name_date) */
    val index1 = index("name_date", (firstName, lastName, birthDate), unique=true)
  }
  /** Collection-like TableQuery object for table Boxers */
  lazy val Boxers = new TableQuery(tag => new Boxers(tag))

  /** Entity class storing rows of table BoxingTypes
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(50,true), Default(None) */
  case class BoxingTypesRow(id: Int, name: Option[String] = None)
  /** GetResult implicit for fetching BoxingTypesRow objects using plain SQL queries */
  implicit def GetResultBoxingTypesRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[BoxingTypesRow] = GR{
    prs => import prs._
    BoxingTypesRow.tupled((<<[Int], <<?[String]))
  }
  /** Table description of table boxing_types. Objects of this class serve as prototypes for rows in queries. */
  class BoxingTypes(_tableTag: Tag) extends Table[BoxingTypesRow](_tableTag, "boxing_types") {
    def * = (id, name) <> (BoxingTypesRow.tupled, BoxingTypesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), name).shaped.<>({r=>import r._; _1.map(_=> BoxingTypesRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(50,true), Default(None) */
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(50,varying=true), O.Default(None))

    /** Uniqueness Index over (name) (database name name) */
    val index1 = index("name", name, unique=true)
  }
  /** Collection-like TableQuery object for table BoxingTypes */
  lazy val BoxingTypes = new TableQuery(tag => new BoxingTypes(tag))

  /** Entity class storing rows of table Fights
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param firstBoxerId Database column first_boxer_id SqlType(INT)
   *  @param secondBoxerId Database column second_boxer_id SqlType(INT)
   *  @param date Database column date SqlType(DATE)
   *  @param place Database column place SqlType(VARCHAR), Length(50,true), Default(None)
   *  @param resultTypeId Database column result_type_id SqlType(INT), Default(None)
   *  @param winner Database column winner SqlType(INT), Default(None)
   *  @param datetimeCreated Database column datetime_created SqlType(TIMESTAMP) */
  case class FightsRow(id: Long, firstBoxerId: Int, secondBoxerId: Int, date: java.sql.Date, place: Option[String] = None, resultTypeId: Option[Int] = None, winner: Option[Int] = None, datetimeCreated: java.sql.Timestamp)
  /** GetResult implicit for fetching FightsRow objects using plain SQL queries */
  implicit def GetResultFightsRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[java.sql.Date], e3: GR[Option[String]], e4: GR[Option[Int]], e5: GR[java.sql.Timestamp]): GR[FightsRow] = GR{
    prs => import prs._
    FightsRow.tupled((<<[Long], <<[Int], <<[Int], <<[java.sql.Date], <<?[String], <<?[Int], <<?[Int], <<[java.sql.Timestamp]))
  }
  /** Table description of table fights. Objects of this class serve as prototypes for rows in queries. */
  class Fights(_tableTag: Tag) extends Table[FightsRow](_tableTag, "fights") {
    def * = (id, firstBoxerId, secondBoxerId, date, place, resultTypeId, winner, datetimeCreated) <> (FightsRow.tupled, FightsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(firstBoxerId), Rep.Some(secondBoxerId), Rep.Some(date), place, resultTypeId, winner, Rep.Some(datetimeCreated)).shaped.<>({r=>import r._; _1.map(_=> FightsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column first_boxer_id SqlType(INT) */
    val firstBoxerId: Rep[Int] = column[Int]("first_boxer_id")
    /** Database column second_boxer_id SqlType(INT) */
    val secondBoxerId: Rep[Int] = column[Int]("second_boxer_id")
    /** Database column date SqlType(DATE) */
    val date: Rep[java.sql.Date] = column[java.sql.Date]("date")
    /** Database column place SqlType(VARCHAR), Length(50,true), Default(None) */
    val place: Rep[Option[String]] = column[Option[String]]("place", O.Length(50,varying=true), O.Default(None))
    /** Database column result_type_id SqlType(INT), Default(None) */
    val resultTypeId: Rep[Option[Int]] = column[Option[Int]]("result_type_id", O.Default(None))
    /** Database column winner SqlType(INT), Default(None) */
    val winner: Rep[Option[Int]] = column[Option[Int]]("winner", O.Default(None))
    /** Database column datetime_created SqlType(TIMESTAMP) */
    val datetimeCreated: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("datetime_created")

    /** Foreign key referencing Boxers (database name FK_fights_boxers) */
    lazy val boxersFk1 = foreignKey("FK_fights_boxers", firstBoxerId, Boxers)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Boxers (database name FK_fights_boxers_2) */
    lazy val boxersFk2 = foreignKey("FK_fights_boxers_2", secondBoxerId, Boxers)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing ResultTypes (database name FK_fights_result_types) */
    lazy val resultTypesFk = foreignKey("FK_fights_result_types", resultTypeId, ResultTypes)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (firstBoxerId,secondBoxerId,date) (database name first_boxer_id) */
    val index1 = index("first_boxer_id", (firstBoxerId, secondBoxerId, date), unique=true)
  }
  /** Collection-like TableQuery object for table Fights */
  lazy val Fights = new TableQuery(tag => new Fights(tag))

  /** Entity class storing rows of table ResultTypes
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(50,true), Default(None) */
  case class ResultTypesRow(id: Int, name: Option[String] = None)
  /** GetResult implicit for fetching ResultTypesRow objects using plain SQL queries */
  implicit def GetResultResultTypesRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[ResultTypesRow] = GR{
    prs => import prs._
    ResultTypesRow.tupled((<<[Int], <<?[String]))
  }
  /** Table description of table result_types. Objects of this class serve as prototypes for rows in queries. */
  class ResultTypes(_tableTag: Tag) extends Table[ResultTypesRow](_tableTag, "result_types") {
    def * = (id, name) <> (ResultTypesRow.tupled, ResultTypesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), name).shaped.<>({r=>import r._; _1.map(_=> ResultTypesRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(50,true), Default(None) */
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(50,varying=true), O.Default(None))

    /** Uniqueness Index over (name) (database name name) */
    val index1 = index("name", name, unique=true)
  }
  /** Collection-like TableQuery object for table ResultTypes */
  lazy val ResultTypes = new TableQuery(tag => new ResultTypes(tag))
}
