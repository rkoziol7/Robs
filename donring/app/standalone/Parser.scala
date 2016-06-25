package standalone

trait Parser {
  def roundValue(value: Float): Option[Float] = {
    val roundNumber =  10000
    
    return Option(((math rint value * roundNumber ) / roundNumber).toFloat)
  }
  
  def convertAmericanToEuropean(americanOdds: Int = 0, americanOddsString: String = ""): Option[Float] = {
    var odds = None: Option[Float]
    if (americanOddsString != "")
    	odds = Some(americanOddsString.toFloat)
    else
      odds = Some(americanOdds.toFloat)
      
    odds = odds match {
      case Some(odds) => if (odds >= 0) roundValue(odds / 100 + 1) else roundValue(100 / (-odds) + 1)
      case None => None
    }
    
    return odds
  }
}