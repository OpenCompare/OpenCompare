package models

import org.opencompare.api.java.PCMContainer
import org.opencompare.api.java.impl.io.KMFJSONLoader
import play.api.libs.json._

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 8/18/15.
 */
object PCMAPIUtils {

  private val jsonLoader : KMFJSONLoader = new KMFJSONLoader()

  /*
    Parse the json file and generate a container
   */
  def createContainers(jsonContent : JsValue) : List[PCMContainer] = {
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
  def saveMetadatas(container : PCMContainer, jsonMetadata : JsObject) {
    val metadata = container.getMetadata()
    val pcm = metadata.getPcm()

    val source = jsonMetadata.value("source").as[JsString].value
    metadata.setSource(source)

    val license = jsonMetadata.value("license").as[JsString].value
    metadata.setLicense(license)

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

}
