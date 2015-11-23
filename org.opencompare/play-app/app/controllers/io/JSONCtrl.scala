package controllers.io

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import controllers.{ResultFormat, ViewContext}
import models.{PCMAPIUtils, User}
import org.opencompare.api.java.impl.io.KMFJSONExporter
import org.opencompare.formalizer.extractor.CellContentInterpreter
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Request, Result}


/**
 * Created by gbecan on 9/21/15.
 */
class JSONCtrl @Inject() (
                           val messagesApi: MessagesApi,
                           val env: Environment[User, CookieAuthenticator],
                           val pcmAPIUtils : PCMAPIUtils) extends IOCtrl("json") {

  val jsonExporter = new KMFJSONExporter()
  val formalizer = new CellContentInterpreter

  val outputParametersForm = Form(
    mapping(
      "file" -> text
    )(JSONExportParameters.apply)(JSONExportParameters.unapply)
  )

  override def importPCMs(format: ResultFormat)(implicit request: Request[AnyContent], viewContext: ViewContext): Result = {
    NotFound("JSON import is not implemented yet")
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
}
