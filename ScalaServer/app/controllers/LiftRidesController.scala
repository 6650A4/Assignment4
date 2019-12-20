package controllers
import javax.inject.Inject
import models.{LiftRides, VerticalDay}
import play.api.Logging
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}
import services.{SkierService, VerticalDayService}

class LiftRidesController @Inject()( val controllerComponents: ControllerComponents) extends BaseController with Logging{
  def getLiftRides(resortID:String,seasonID:String,dayID:String,skierID:String) = Action{ implicit request: Request[AnyContent] =>
    val Pk:String = skierID.toString() + "/" + resortID.toString()+ "/"+ seasonID + "/" + dayID
    val verticalService = new VerticalDayService()
    val res:String = verticalService.getVerticalDay(Pk).toString()
    Ok(res)
  }
  def postLiftRides(resortID:String,seasonID:String,dayID:String,skierID:String) = Action{ implicit request: Request[AnyContent] =>
    val bodyArray = request.body.toString().split(",")
    val time:Int = bodyArray(0).split(":")(1).toInt
    val rawLiftID:String = bodyArray(1).split(":")(1)
    val liftID:Int = rawLiftID.slice(0,rawLiftID.length()-2).toInt
    val PK:String = skierID + "/" + seasonID + "/" + dayID+ "/" + time
    val liftRide = new LiftRides(skierID.toInt,resortID.toInt,seasonID.toInt,dayID.toInt,time,liftID,PK)
    val skierService = new SkierService()
    skierService.postSkier(liftRide)
    val PkVertical:String = skierID.toString() + "/" + resortID.toString()+ "/"+ seasonID.toString() + "/" + dayID.toString()
    val verticalDay = new VerticalDay(PkVertical,liftID*10)
    val verticalService = new VerticalDayService()
    verticalService.updateVertical(verticalDay)
    Ok("Successfully posted")
  }
}
