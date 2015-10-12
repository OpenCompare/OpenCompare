package models.daos

import com.mongodb.casbah.Imports._
import models.DatabasePCM
import org.bson.types.ObjectId
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

trait PCMContainerDAO {

  def get(id : String) : Future[Option[DatabasePCM]]

}
