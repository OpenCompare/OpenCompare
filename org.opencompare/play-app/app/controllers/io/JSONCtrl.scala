package controllers.io

import javax.inject.Inject
import java.io.File

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import controllers.{ResultFormat, ViewContext}
import models.{PCMAPIUtils, User}
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.KMFJSONExporter
import org.opencompare.api.java.extractor.CellContentInterpreter
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Request, Result}
import org.opencompare.api.java.impl.io.KMFJSONLoader

import scala.collection.JavaConversions._


/**
 * Created by gbecan on 9/21/15.
 */
class JSONCtrl @Inject() (
                           val messagesApi: MessagesApi,
                           val env: Environment[User, CookieAuthenticator],
                           val pcmAPIUtils : PCMAPIUtils) extends IOCtrl("json") {

  val jsonExporter = new KMFJSONExporter()
  val formalizer = new CellContentInterpreter(new PCMFactoryImpl)

  val inputParametersForm = Form(
    mapping(
      "title" -> nonEmptyText
    )(JSONImportParameters.apply)(JSONImportParameters.unapply)
  )

  val outputParametersForm = Form(
    mapping(
      "file" -> text
    )(JSONExportParameters.apply)(JSONExportParameters.unapply)
  )

  override def importPCMs(format: ResultFormat)(implicit request: Request[AnyContent], viewContext: ViewContext): Result = {

  // Parse parametersOC
  val parameters = inputParametersForm.bindFromRequest.get

  val title = parameters.title
  // Read input file
  val file = request.body.asMultipartFormData.get.file("file").get.ref.file


  try {

    val loader = new KMFJSONLoader()
    val pcmContainers = loader.load(file).toList
    val pcmContainer = pcmContainers.head
    pcmContainer.getPcm.setName(title)

    // Serialize result
    val jsonResult = postprocessContainers(pcmContainers)
    Ok(jsonResult)

  } catch {
    case e : Exception => BadRequest("This file is invalid")
  }
  }

  override def exportPCM(implicit request: Request[AnyContent]): Result = {
    val parameters = outputParametersForm.bindFromRequest.get
    val pcmJSON = Json.parse(parameters.pcm)

    val container = pcmAPIUtils.parsePCMContainers(pcmJSON).head
    formalizer.interpretCells(container.getPcm)
    val json = jsonExporter.export(container)

    Ok(json)
  }

  case class JSONExportParameters(pcm : String)

  case class JSONImportParameters(
    title : String
  )
}
