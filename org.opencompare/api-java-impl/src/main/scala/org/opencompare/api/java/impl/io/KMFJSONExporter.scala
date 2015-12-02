package org.opencompare.api.java.impl.io

import org.opencompare.api.java.PCMContainer
import org.opencompare.api.java.impl.PCMImpl
import org.opencompare.api.java.io.PCMExporter
import org.opencompare.model.pcm.factory.DefaultPcmFactory
import play.api.libs.json.{JsObject, JsValue, Json}

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
      var json = ""

      if (container.getPcm().isInstanceOf[PCMImpl]) {

          val pcm = container.getPcm().asInstanceOf[PCMImpl]

          // Convert all strings to base64 to avoid encoding problems
          if (base64Encoding) {
              encoder.encode(pcm)
          }

          // Serialize PCM
          val kPcm = pcm.getKpcm()
          json = serializer.serialize(kPcm)

          // Decode PCM
          if (base64Encoding) {
              encoder.decode(pcm)
          }
      }

      val jsonPCM = Json.parse(json)

      Json.stringify(JsObject(Map(
          "pcm" -> jsonPCM,
          "metadata" -> JsObject(Map.empty[String, JsValue])
      )))
  }
}
