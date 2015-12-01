package controllers.io

import java.io.IOException
import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import controllers.{ResultFormat, ViewContext}
import models.{PCMAPIUtils, User}
import org.opencompare.api.java.PCMFactory
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{CSVExporter, CSVLoader}
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.JavaConversions._
import scala.io.Source

/**
 * Created by gbecan on 8/18/15.
 */
class CSVCtrl @Inject() (
                          val messagesApi: MessagesApi,
                          val env: Environment[User, CookieAuthenticator],
                          val pcmAPIUtils : PCMAPIUtils) extends IOCtrl("csv") {

  private val pcmFactory : PCMFactory = new PCMFactoryImpl()
  private val csvExporter : CSVExporter= new CSVExporter()

  val inputParametersForm = Form(
    mapping(
      "productAsLines" -> boolean,
      "title" -> nonEmptyText,
      "separator" -> nonEmptyText(1, 1),
      "quote" -> nonEmptyText(1, 1)
    )(CSVImportParameters.apply)(CSVImportParameters.unapply)
  )

  val outputParametersForm = Form(
    mapping(
      "productAsLines" -> boolean,
      "file" -> text,
      "separator" -> nonEmptyText(1, 1),
      "quote" -> nonEmptyText(1, 1)
    )(CSVExportParameters.apply)(CSVExportParameters.unapply)
  )

  override def importPCMs(format : ResultFormat)(implicit request: Request[AnyContent], viewContext: ViewContext) : Result = {
    // Parse parameters
    val parameters = inputParametersForm.bindFromRequest.get
    val separator = parameters.separator.head
    val quote = parameters.quote.head

    // Read input file
    val file = request.body.asMultipartFormData.get.file("file").get.ref.file

    try {
      val loader: CSVLoader = new CSVLoader(pcmFactory, separator, quote, parameters.productAsLines)
      val pcmContainers = loader.load(file).toList
      val pcmContainer = pcmContainers.head
      pcmContainer.getPcm.setName(parameters.title)

      // Serialize result
      val jsonResult = postprocessContainers(pcmContainers)
      Ok(jsonResult)

    } catch {
      case e : Exception => BadRequest("This file is invalid")
    }

  }

  override def exportPCM(implicit request : Request[AnyContent]) : Result = {
    val parameters = outputParametersForm.bindFromRequest().get

    val separator = parameters.separator.head
    val quote = parameters.quote.head

    val jsonPCM = Json.parse(parameters.pcm)
    val container = pcmAPIUtils.parsePCMContainers(jsonPCM).head
    container.getMetadata.setProductAsLines(parameters.productAsLines)

    val csvCode = csvExporter.export(container, separator, quote)

    Ok(csvCode)
  }

}

case class CSVImportParameters(
  productAsLines : Boolean,
  title : String,
  separator : String,
  quote : String
)

case class CSVExportParameters(
  productAsLines : Boolean,
  pcm : String,
  separator : String,
  quote : String
)