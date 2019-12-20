package services
import models.{VerticalDay}

class VerticalDayService{
  def updateVertical(verticalDay:VerticalDay) = {
    val db = DBCP.connectionPool
    val conn = db.getConnection()
    val insertQueryStatement = "INSERT INTO VerticalDay (PK, total) " + "VALUES (?,?) " + "ON DUPLICATE KEY UPDATE total=total+VALUES(total)";
    val preparedStatement = conn.prepareStatement(insertQueryStatement)
    try {
      preparedStatement.setString(1, verticalDay.getPK())
      preparedStatement.setInt(2, verticalDay.getVertical())
      preparedStatement.executeUpdate
      preparedStatement.close
      conn.close
    }
    catch {
      case e:Exception => e.printStackTrace()
    }
    finally{
      preparedStatement.close
      conn.close
    }
  }
  def getVerticalDay(Pk:String):Int = {
    val db = DBCP.connectionPool
    val conn = db.getConnection()
    val selectQueryStatement = "SELECT total FROM VerticalDay WHERE VerticalDay.PK = ?"
    var preparedStatement = conn.prepareStatement(selectQueryStatement)
    try {
      preparedStatement.setString(1, Pk)
      // execute insert SQL statement
      val rs = preparedStatement.executeQuery
      while ( {
        rs.next
      }) {
        val ans = rs.getInt("total")
        preparedStatement.close
        conn.close
        return ans
      }
      rs.close
    } catch {
      case e: Exception => e.printStackTrace()
    }
    preparedStatement.close
    conn.close
    -1
  }
}
