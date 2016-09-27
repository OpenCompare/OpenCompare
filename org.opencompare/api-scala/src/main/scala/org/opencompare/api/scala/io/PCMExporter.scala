package org.opencompare.api.scala.io

import org.opencompare.api.scala.PCM

trait PCMExporter {

  /**
    * Export a PCM into a specific format
    * @param pcm PCM to export
    * @return string representing the PCM
    */
  def export(pcm: PCM): String

}
