package org.opencompare.api.scala.io

import java.io.File

import org.opencompare.api.scala.PCM

trait PCMLoader {
  /**
    * Return a list of PCMs from a string representation
    *
    * @param pcms : string representation of a PCM
    * @return the PCM represented by pcm
    */
  def load(pcms : String) : List[PCM]

  /**
    * Return a list of PCMs from a file
    *
    * @param file file to load
    * @return loaded PCM
    */
  def load(file : File) : List[PCM]

}
