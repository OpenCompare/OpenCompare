package models

import java.util.UUID
import javax.inject.{Singleton, Inject}

import models.daos.UserDAO
import org.opencompare.api.java.{PCM, PCMMetadata, PCMContainer}
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import play.api.libs.json._

import scala.collection.JavaConversions._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by gbecan on 8/18/15.
 */
@Singleton
class PCMAPIUtils @Inject() (userDAO : UserDAO) {

  private val jsonLoader : KMFJSONLoader = new KMFJSONLoader()
  private val kmfExporter: KMFJSONExporter = new KMFJSONExporter

  /*
    Parse the json file and generate a container
   */
  def parsePCMContainers(jsonContent : JsValue) : List[PCMContainer] = {
    val jsonObject = jsonContent.as[JsObject]
    val jsonPCM = Json.stringify(jsonObject.value("pcm"))
    val containers = jsonLoader.load(jsonPCM).toList
    containers
  }

  def serializePCMContainer(pcmContainer : PCMContainer) : Future[JsValue] = {
    val pcm = pcmContainer.getPcm
    val metadata = pcmContainer.getMetadata

    // Serialize PCM
    val jsonContainer = Json.parse(kmfExporter.export(pcmContainer)).as[JsObject]


    // Retrieve full name of creator
    val uuid = try {
      Some(UUID.fromString(metadata.getCreator))
    } catch {
      case e : IllegalArgumentException => None
    }

    val futureCreatorFullName = if (uuid.isDefined) {
      val futureCreatorInfo = userDAO.find(uuid.get)

      futureCreatorInfo map { creatorInfo =>
        if (creatorInfo.isDefined && creatorInfo.get.fullName.isDefined) {
          val fullName = creatorInfo.get.fullName.get

          jsonContainer ++ JsObject(Map(
            "metadata" -> JsObject(Map(
              "creatorFullName" -> JsString(fullName)
            ))
          ))
        } else {
          jsonContainer
        }
      }
    } else {
      Future.successful(jsonContainer)
    }


    futureCreatorFullName map { json =>
      json
    }
  }

}
