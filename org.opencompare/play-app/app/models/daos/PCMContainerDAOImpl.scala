package models.daos

import java.util.Base64

import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import models.{Database, DatabasePCM}
import org.bson.types.ObjectId
import org.opencompare.api.java.PCMContainer
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import models.daos.PCMContainerDAOImpl._
import scala.concurrent.Future
import scala.collection.JavaConversions._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by gbecan on 10/12/15.
 */
class PCMContainerDAOImpl extends PCMContainerDAO {


  override def get(id: String): Future[Option[DatabasePCM]] = {
    if (ObjectId.isValid(id)) {
      Future {
        val searchById = MongoDBObject("_id" -> new ObjectId(id))
        val result = pcms.findOne(searchById)

        if (result.isDefined) {
          val databasePCM = convertToPCMContainers(result.get)
          Some(databasePCM)
        } else {
          None
        }
      }
    } else {
      Future.successful(None)
    }
  }



  private def convertToPCMContainers(dbObject : DBObject) : DatabasePCM = {
    val id = dbObject("_id").toString
    val json = JSON.serialize(dbObject)

    val pcmContainers = kmfLoader.load(json)

    if (pcmContainers.size == 1) {
      val pcmContainer = pcmContainers.head
      new DatabasePCM(Some(id), Some(pcmContainer))

    } else {
      new DatabasePCM(None, None)
    }
  }

}

object PCMContainerDAOImpl {
  val kmfLoader: KMFJSONLoader = new KMFJSONLoader
  val kmfSerializer: KMFJSONExporter = new KMFJSONExporter

  val base64Decoder = Base64.getDecoder

  val pcms = Database.db("pcms")

}