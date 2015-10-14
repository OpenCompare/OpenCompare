package controllers.io

import java.io.IOException
import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import controllers._
import models.{Database, PCMAPIUtils, User}
import org.opencompare.api.java.PCMFactory
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{HTMLExporter, HTMLLoader}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 8/18/15.
 */
@Singleton
class HTMLCtrl @Inject() (
                           val messagesApi: MessagesApi,
                           val env: Environment[User, CookieAuthenticator],
                           val pcmAPIUtils : PCMAPIUtils) extends IOCtrl("html") {

  private val pcmFactory: PCMFactory = new PCMFactoryImpl
  private val htmlExporter: HTMLExporter = new HTMLExporter

  val inputParametersForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "productAsLines" -> boolean,
      "content" -> nonEmptyText,
      "source" -> optional(text)
    )(HTMLImportParameters.apply)(HTMLImportParameters.unapply)
  )

  val outputParametersForm = Form(
    mapping(
      "productAsLines" -> boolean,
      "file" -> text
    )(HTMLExportParameters.apply)(HTMLExportParameters.unapply)
  )

  override def importPCMs(format: ResultFormat)(implicit request: Request[AnyContent], viewContext: ViewContext): Result = {
    try {
      // Parse parameters
      val parameters = inputParametersForm.bindFromRequest.get

      val loader = new HTMLLoader(pcmFactory, parameters.productAsLines)
      val pcmContainers = loader.load(parameters.content).toList
      normalizeContainers(pcmContainers)

      if (pcmContainers.isEmpty) {
        NotFound("No matrices were found in this html page")
      } else {
        val pcmContainer = pcmContainers.head
        pcmContainer.getPcm.setName(parameters.title)
        if (parameters.source.isDefined) {
          pcmContainer.getMetadata.setSource(parameters.source.get)
        }


        format match {
          case JsonFormat() => Ok(postprocessContainers(pcmContainers))
          case EmbedFormat() =>
            val jsonResult = Json.parse(Database.serializePCMContainerToJSON(pcmContainer)) // FIXME : ugly ugly ugly !!!! BAHHHHHHHHH !!!
            Database.addHTMLSource(parameters.source.get)
            Ok(views.html.embed(null, jsonResult, null))
          case PageFormat() =>
            val jsonResult = Json.parse(Database.serializePCMContainerToJSON(pcmContainer)) // FIXME : ugly ugly ugly !!!! BAHHHHHHHHH !!!
            Database.addHTMLSource(parameters.source.get)
            Ok(views.html.edit(null, jsonResult, null))
        }


      }
    } catch {
      case e: Exception => BadRequest("An error occured during the import.")
    }

  }

  override def exportPCM(implicit request: Request[AnyContent]): Result = {
    val parameters = outputParametersForm.bindFromRequest.get
    val pcmJSON = Json.parse(parameters.pcm)

    val container = pcmAPIUtils.parsePCMContainers(pcmJSON).head
    container.getMetadata.setProductAsLines(parameters.productAsLines)

    val html = htmlExporter.export(container)

    Ok(html)
  }

}

case class HTMLImportParameters(
                               title : String,
                               productAsLines : Boolean,
                               content: String,
                               source: Option[String]
                                      )

case class HTMLExportParameters(
                                      productAsLines : Boolean,
                                      pcm : String
                                      )