package services
import models.LiftRides
class SkierService{
  def postSkier(liftRide:LiftRides) = {
    val conn = DBCP.connectionPool.getConnection()
    val insertQueryStatement = "INSERT IGNORE INTO LiftRides (skierId, resortId, seasonId, dayId, time, liftId, PK) " + "VALUES (?,?,?,?,?,?,?) "
    var preparedStatement = conn.prepareStatement(insertQueryStatement)
    try {
      preparedStatement = conn.prepareStatement(insertQueryStatement)
      preparedStatement.setInt(1, liftRide.getSkierId)
      preparedStatement.setInt(2, liftRide.getResortId)
      preparedStatement.setInt(3, liftRide.getSeasonId)
      preparedStatement.setInt(4, liftRide.getDayId)
      preparedStatement.setInt(5, liftRide.getTime)
      preparedStatement.setInt(6, liftRide.getLiftId)
      preparedStatement.setString(7, liftRide.getPK)
      preparedStatement.executeUpdate
    }
    catch {
      case e:Exception => e.printStackTrace()
    }
    finally{
      preparedStatement.close()
      conn.close()
    }
  }
}
