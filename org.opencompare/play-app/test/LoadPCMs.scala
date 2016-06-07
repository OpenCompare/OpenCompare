import java.io.{File, FilenameFilter}
import java.util.List

import models.Database
import org.junit.Test
import org.opencompare.api.java.impl.io.KMFJSONLoader

/**
  * Created by gbecan on 12/12/14.
  */
class LoadPCMs {

  @Test
  def testLoadWikipediaPCMs() {
    val path = "../dataset-wikipedia/output/formalized/model"
    val dir = new File(path)

    val loader = new KMFJSONLoader()

    for (file <- dir.listFiles.filter(_.getName.endsWith(".pcm"))) {
      // Load PCM
      val pcmContainers = loader.load(file)
      val pcmContainer = pcmContainers.get(0)
      val pcm = pcmContainer.getPcm()
      val metadata = pcmContainer.getMetadata()

      // Set source and license
      val hyphenIndex = pcm.getName.indexOf("-")
      val pageName = if (hyphenIndex >= 0) {
        pcm.getName.substring(0, hyphenIndex - 1)
      } else {
        pcm.getName
      }

      val source = "https://en.wikipedia.org/wiki/" + pageName
      metadata.setSource(source)
      metadata.setLicense("Creative Commons Attribution-ShareAlike 3.0 Unported")

      // Remove underscores in name
      pcm.setName(pcm.getName().replaceAll("_", " "))

      // Add to database if valid
      if (pcm.isValid()) {
        Database.create(pcmContainer)
      }
    }

  }

}
