package org.opencompare.api.java.impl.io

import java.io.{File, FileInputStream}

import org.opencompare.api.java.impl.{FeatureImpl, ProductImpl, PCMImpl}
import org.opencompare.api.java.io.PCMLoader
import org.opencompare.api.java.{PCMContainer, PCMMetadata}
import org.opencompare.model.pcm.factory.DefaultPcmFactory
import play.api.libs.json._

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 12/12/14.
 */
class KMFJSONLoader(val base64Decoding: Boolean = true) extends PCMLoader {

  private val kpcmFactory = new DefaultPcmFactory
  private val loader = kpcmFactory.createJSONLoader
  private val encoder = new PCMBase64Encoder

  def this() {
      this(true)
  }

  override def load(json: String): java.util.List[PCMContainer] = {
    val jsonPCMContainer = Json.parse(json).as[JsObject]
    load(jsonPCMContainer)
  }

  override def load(file: File): java.util.List[PCMContainer] = {
    val inputStream = new FileInputStream(file)
    val jsonPCMContainer = Json.parse(inputStream).as[JsObject]
    inputStream.close()
    load(jsonPCMContainer)
  }

  private def load(jsonPCMContainer : JsObject) : List[PCMContainer] = {
    val jsonPCM = jsonPCMContainer.value("pcm").as[JsObject]
    val jsonMetadata = jsonPCMContainer.value("metadata").as[JsObject]


    val containers = loader.loadModelFromString(Json.stringify(jsonPCM)).toList

    for (container <- containers) yield {
      val containerPCM = new PCMContainer()

      // Load PCM
      val pcm = new PCMImpl(container.asInstanceOf[org.opencompare.model.PCM])
      encoder.decode(pcm)
      containerPCM.setPcm(pcm)

      // Load metadata
      val metadata = new PCMMetadata(pcm)
      containerPCM.setMetadata(metadata)


      val source = jsonMetadata.value.get("source")
      if (source.isDefined) {
        metadata.setSource(source.get.as[String])
      }

      val license = jsonMetadata.value.get("license")
      if (license.isDefined) {
        metadata.setLicense(license.get.as[String])
      }

      val creator = jsonMetadata.value.get("creator")
      creator match {
        case Some(JsString(value)) => metadata.setCreator(value)
        case _ =>
      }


      val jsonProductPositions = jsonMetadata.value("productPositions").as[JsArray]
      val jsonFeaturePositions = jsonMetadata.value("featurePositions").as[JsArray]

      for (jsonProductPosition <- jsonProductPositions.value) {
        val jsonPos = jsonProductPosition.as[JsObject].value
        val productName = jsonPos("product").as[JsString].value
        val position = jsonPos("position").as[JsNumber].value.toIntExact

        val product = pcm.getProducts.find { p =>
          val id = p.asInstanceOf[ProductImpl].getkProduct().getGenerated_KMF_ID()
          id == productName
        }
        if (product.isDefined) {
          metadata.setProductPosition(product.get, position)
        }

      }

      for (jsonFeaturePosition <- jsonFeaturePositions.value) {
        val jsonPos = jsonFeaturePosition.as[JsObject].value
        val featureName = jsonPos("feature").as[JsString].value
        val position = jsonPos("position").as[JsNumber].value.toIntExact

        val feature = pcm.getConcreteFeatures.find { f =>
          val id = f.asInstanceOf[FeatureImpl].getkFeature().getGenerated_KMF_ID()
          id == featureName
        }
        if (feature.isDefined) {
          metadata.setFeaturePosition(feature.get, position)
        }

      }


      containerPCM
    }
  }
}

