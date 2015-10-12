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
    val jsonMetadata = jsonObject.value("metadata").as[JsObject]
    for (container <- containers) {
      saveMetadatas(container, jsonMetadata)
    }
    containers
  }

  /*
    Insert metadatas inside the container based on the json metadatas
   */
  private def saveMetadatas(container : PCMContainer, jsonMetadata : JsObject) {
    val metadata = container.getMetadata()
    val pcm = metadata.getPcm()

    val source = jsonMetadata.value.get("source")
    if (source.isDefined) {
      metadata.setSource(source.get.as[String])
    }

    val license = jsonMetadata.value.get("license")
    if (license.isDefined) {
      metadata.setLicense(license.get.as[String])
    }

    val creator = jsonMetadata.value.get("creator")
    println(creator)
    if (creator.isDefined) {
      metadata.setCreator(creator.get.as[String])
    }


    val jsonProductPositions = jsonMetadata.value("productPositions").as[JsArray]
    val jsonFeaturePositions = jsonMetadata.value("featurePositions").as[JsArray]

    for (jsonProductPosition <- jsonProductPositions.value) {
      val jsonPos = jsonProductPosition.as[JsObject].value
      val productName = jsonPos("product").as[JsString].value
      val position = jsonPos("position").as[JsNumber].value.toIntExact

      val product = pcm.getProducts.find(_.getName == productName)  // FIXME : equals based on name breaks same name products
      if (product.isDefined) {
        metadata.setProductPosition(product.get, position)
      }

    }

    for (jsonFeaturePosition <- jsonFeaturePositions.value) {
      val jsonPos = jsonFeaturePosition.as[JsObject].value
      val featureName = jsonPos("feature").as[JsString].value
      val position = jsonPos("position").as[JsNumber].value.toIntExact

      val feature = pcm.getConcreteFeatures.find(_.getName == featureName) // FIXME : equals based on name breaks same name features
      if (feature.isDefined) {
        metadata.setFeaturePosition(feature.get, position)
      }

    }
  }

  def serializePCMContainer(pcmContainer : PCMContainer) : Future[JsValue] = {
    val pcm = pcmContainer.getPcm
    val metadata = pcmContainer.getMetadata

    // Serialize PCM
    val jsonPCM = Json.parse(kmfExporter.export(pcmContainer))

    // Serialize metadata
    val futureJsonMetadata = serializeMetadata(pcm, metadata)


    futureJsonMetadata map { jsonMetadata =>
      JsObject(Seq(
        "pcm" -> jsonPCM,
        "metadata" -> jsonMetadata
      ))
    }
  }

  private def serializeMetadata(pcm : PCM, metadata : PCMMetadata) : Future[JsValue] = {
    // Serialize product positions
    val productPositions = JsArray(for (product <- pcm.getProducts) yield {
      val productName = product.getName
      val position = metadata.getProductPosition(product)
      JsObject(Seq(
        "product" -> JsString(productName),
        "position" -> JsNumber(position)
      ))
    })

    // Serialize feature positions
    val featurePositions = JsArray(for (feature <- pcm.getConcreteFeatures) yield {
      val featureName = feature.getName
      val position = metadata.getFeaturePosition(feature)
      JsObject(Seq(
        "feature" -> JsString(featureName),
        "position" -> JsNumber(position)
      ))
    })

    val jsonMetadata = JsObject(Seq(
      "productPositions" -> productPositions,
      "featurePositions" -> featurePositions,
      "source" -> JsString(metadata.getSource),
      "license" -> JsString(metadata.getLicense),
      "creator" -> JsString(metadata.getCreator)
    ))

    val futureCreatorInfo = userDAO.find(UUID.fromString(metadata.getCreator))

    futureCreatorInfo map { creatorInfo =>
      if (creatorInfo.isDefined && creatorInfo.get.fullName.isDefined) {
        val fullName = creatorInfo.get.fullName.get
        jsonMetadata + ("creatorFullName" -> JsString(fullName))
      } else {
        jsonMetadata
      }
    }
  }

}
