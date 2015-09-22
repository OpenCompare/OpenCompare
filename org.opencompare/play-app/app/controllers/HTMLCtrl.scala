package controllers

import java.io.IOException

import model.{Database, PCMAPIUtils}
import org.opencompare.api.java.PCMFactory
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{HTMLLoader, CSVLoader, HTMLExporter}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.io.Source

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 8/18/15.
 */
class HTMLCtrl extends IOCtrl {

  private val pcmFactory: PCMFactory = new PCMFactoryImpl
  private val htmlExporter: HTMLExporter = new HTMLExporter

  val inputParametersForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "productAsLines" -> boolean,
      "content" -> text
    )(HTMLImportParameters.apply)(HTMLImportParameters.unapply)
  )

  val outputParametersForm = Form(
    mapping(
      "productAsLines" -> boolean,
      "file" -> text
    )(HTMLExportParameters.apply)(HTMLExportParameters.unapply)
  )

  val embedParametersForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "productAsLines" -> boolean
    )(EmbedHTMLParameters.apply)(EmbedHTMLParameters.unapply)
  )

  override def importPCMs(implicit request: Request[AnyContent]): Result = {
    // Parse parameters
    val parameters = inputParametersForm.bindFromRequest.get

    try {
      val loader = new HTMLLoader(pcmFactory, parameters.productAsLines)
      val pcmContainers = loader.load(parameters.content).toList
      val pcmContainer = pcmContainers.head

      pcmContainer.getPcm.setName(parameters.title)

      // Serialize result
      val jsonResult = postprocessContainers(pcmContainers)
      Ok(jsonResult)

    } catch {
      case e : IOException => BadRequest("This file is invalid")
    }
  }

  override def exportPCM(implicit request: Request[AnyContent]): Result = {
    val parameters = outputParametersForm.bindFromRequest.get
    val pcmJSON = Json.parse(parameters.pcm)

    val container = PCMAPIUtils.createContainers(pcmJSON).head
    container.getMetadata.setProductAsLines(parameters.productAsLines)

    val html = htmlExporter.export(container)

    Ok(html)
  }

  def embedFromHTML() = Action { implicit request =>
    // Parse parameters
    val parameters = embedParametersForm.bindFromRequest.get

    // Read input file
    val file = request.body.asMultipartFormData.get.file("file").get
    val htmlData = Source.fromFile(file.ref.file).getLines().mkString("\n")

    val loader: HTMLLoader = new HTMLLoader(pcmFactory, parameters.productAsLines)
    val pcmContainers = loader.load(htmlData).toList

    try {
      val loader = new HTMLLoader(pcmFactory, parameters.productAsLines)
      val pcmContainers = loader.load(htmlData).toList
      normalizeContainers(pcmContainers)

      if (pcmContainers.isEmpty) {
        NotFound("No matrices were found in this html page")
      } else {
        val pcmContainer = pcmContainers.head
        pcmContainer.getPcm.setName(parameters.title)

        val id: String = Database.create(pcmContainer)

        Ok(id)
      }
    } catch {
      case e : IOException => BadRequest("This file is invalid")
    }
  }
}

case class HTMLImportParameters(
                               title : String,
                               productAsLines : Boolean,
                               content: String
                                      )

case class HTMLExportParameters(
                                      productAsLines : Boolean,
                                      pcm : String
                                      )
case class EmbedHTMLParameters(
                                title : String,
                                productAsLines : Boolean
                                )