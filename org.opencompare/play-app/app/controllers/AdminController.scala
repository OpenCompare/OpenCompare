package controllers

import java.io.File
import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.{Database, AdminAuthorization, User}
import org.opencompare.api.java.impl.io.KMFJSONLoader
import play.api.i18n.MessagesApi

/**
 * Created by gbecan on 10/1/15.
 */
class AdminController @Inject() (val messagesApi: MessagesApi, val env: Environment[User, CookieAuthenticator]) extends BaseController {

  def load(pcmType : String) = SecuredAction(AdminAuthorization()) {

    val path = "/var/www/opencompare/loading"
    val dir = new File(path)

    val loader = new KMFJSONLoader()

    var nbOfLoadedPCMs = 0

    if (dir.exists())  {
      for (file <- dir.listFiles.filter(_.getName.endsWith(".pcm"))) {
        // Load PCM
        val pcmContainers = loader.load(file)
        val pcmContainer = pcmContainers.get(0)
        val pcm = pcmContainer.getPcm()
        val metadata = pcmContainer.getMetadata()

        // Set source and license
        pcmType match {
          case "wikipedia" =>
            val hyphenIndex = pcm.getName.indexOf("-")
            val pageName = if (hyphenIndex >= 0) {
              pcm.getName.substring(0, hyphenIndex - 1)
            } else {
              pcm.getName
            }

            val source = "https://en.wikipedia.org/wiki/" + pageName
            metadata.setSource(source)
            metadata.setLicense("Creative Commons Attribution-ShareAlike 3.0 Unported")

            pcm.setName(pcm.getName().replaceAll("_", " ")) // Remove underscores in name
          case _ =>
        }


        // Add to database if valid
        if (pcm.isValid()) {
          Database.create(pcmContainer)
          nbOfLoadedPCMs += 1
        }
      }
    }

    Ok(nbOfLoadedPCMs + " pcms successfully loaded.")
  }
}
