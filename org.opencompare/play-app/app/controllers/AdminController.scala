package controllers

import java.io.File
import javax.inject.Inject

import models.daos.PCMContainerDAO

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.{Database, AdminAuthorization, User}
import org.opencompare.api.java.impl.io.KMFJSONLoader
import play.api.i18n.MessagesApi

import scala.concurrent._
import ExecutionContext.Implicits.global

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.{PCMContainer, PCMFactory}
import org.opencompare.api.java.extractor.CellContentInterpreter
import models._

import play.api.Logger

/**
 * Created by gbecan on 10/1/15.
 */
class AdminController @Inject() (val messagesApi: MessagesApi,
                                val pcmContainerDAO: PCMContainerDAO,
                                val pcmAPIUtils : PCMAPIUtils,
                                val env: Environment[User, CookieAuthenticator])
                                extends BaseController {


  private val pcmFactory : PCMFactory = new PCMFactoryImpl()
  private val cellContentInterpreter: CellContentInterpreter = new CellContentInterpreter(pcmFactory)

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


  // TODO move to PCMAPI
  // in the long run: provide a procedure to re-type a PCM 
  def retype(pcmid : String) = UserAwareAction { implicit request =>

    val exists = Database.exists(pcmid)
    if (exists) {
        val result = pcmContainerDAO.get(pcmid)

        result foreach { dbPCM =>
          if (dbPCM.isDefined) {
            val pcmContainer = dbPCM.get.pcmContainer.get
            val pcm = pcmContainer.getPcm
            pcm.normalize(pcmFactory)
           // cellContentInterpreter.interpretCells(pcm)
            cellContentInterpreter.interpretCellsFromScratch(pcm)

            val databasePCM = new DatabasePCM(Some(pcmid), Some(pcmContainer))
            Database.update(databasePCM)

          } else {
            //
          }
        }

        Ok(views.html.edit(pcmid, null, null))
    } else {
        Ok(views.html.edit(null, null, null))
    }

  }

}
