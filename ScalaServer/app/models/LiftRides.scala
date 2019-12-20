package models
class LiftRides(skierId:Int, resortId:Int, seasonId:Int, dayId:Int, time:Int, liftId:Int, PK:String){
  def getSkierId(): Int = skierId
  def getResortId(): Int = resortId
  def getSeasonId(): Int = seasonId
  def getDayId(): Int = dayId
  def getTime(): Int = time
  def getLiftId(): Int = liftId
  def getPK(): String = PK
}
