package org.opencompare.api.java.impl.io

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util

import org.opencompare.api.java.{PCMMetadata, PCMContainer}
import org.opencompare.api.java.impl.PCMImpl
import org.opencompare.api.java.io.PCMLoader
import org.opencompare.model.pcm.factory.DefaultPcmFactory

import collection.JavaConversions._

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

  override def load(json: String): util.List[PCMContainer] = {
    val containers = loader.loadModelFromString(json).toList
    load(containers)
  }

  override def load(file: File): util.List[PCMContainer] = {
    val bytes = Files.readAllBytes(file.toPath())
    val json = new String(bytes, StandardCharsets.UTF_8)
    load(json)
  }

  private def load(containers : List[org.kevoree.modeling.api.KMFContainer] ) : List[PCMContainer] = {
    for (container <- containers) yield {
      val pcm = new PCMImpl(container.asInstanceOf[org.opencompare.model.PCM])
      encoder.decode(pcm)
      val containerPCM = new PCMContainer()
      val metadata = new PCMMetadata(pcm)
      containerPCM.setPcm(pcm)
      containerPCM.setMetadata(metadata)
      containerPCM
    }
  }
}

