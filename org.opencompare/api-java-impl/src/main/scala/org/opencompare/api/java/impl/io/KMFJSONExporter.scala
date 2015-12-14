package org.opencompare.api.java.impl.io

import org.opencompare.api.java.{Product, Feature, PCMContainer}
import org.opencompare.api.java.impl.{FeatureImpl, ProductImpl, PCMImpl}
import org.opencompare.api.java.io.PCMExporter
import org.opencompare.model.pcm.factory.DefaultPcmFactory
import play.api.libs.json._

import collection.JavaConversions._

/**
 * Created by gbecan on 13/10/14.
 */
class KMFJSONExporter(val base64Encoding : Boolean = true) extends PCMExporter {

  def this() {
    this(true)
  }

  private val factory = new DefaultPcmFactory
  private val serializer = factory.createJSONSerializer
  private val encoder = new PCMBase64Encoder

  override def export(container: PCMContainer): String = {
    container.getPcm match {
      case pcm: PCMImpl =>
        val metadata = container.getMetadata

        // Serialize Metadata

        // Serialize product positions
        val productPositions = JsArray(for (product <- pcm.getProducts) yield {
          val productId = product.asInstanceOf[ProductImpl].getkProduct().getGenerated_KMF_ID()
          val position = metadata.getProductPosition(product)
          JsObject(Seq(
            "product" -> JsString(productId),
            "position" -> JsNumber(position)
          ))
        })

        // Serialize feature positions
        val featurePositions = JsArray(for (feature <- pcm.getConcreteFeatures) yield {
          val featureId = feature.asInstanceOf[FeatureImpl].getkFeature().getGenerated_KMF_ID()
          val position = metadata.getFeaturePosition(feature)
          JsObject(Seq(
            "feature" -> JsString(featureId),
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


        // Convert all strings to base64 to avoid encoding problems
        if (base64Encoding) {
          encoder.encode(pcm)
        }

        // Serialize PCM
        val kPcm = pcm.getKpcm()

        val jsonPCM = serializer.serialize(kPcm)

        // Decode PCM
        if (base64Encoding) {
          encoder.decode(pcm)
        }

        List(
          "{",
          " \"pcm\" : " + jsonPCM + ",",
          " \"metadata\" : " + Json.prettyPrint(jsonMetadata),
          "}"
        ).mkString("\n")

      case _ => ""
    }
  }
}
