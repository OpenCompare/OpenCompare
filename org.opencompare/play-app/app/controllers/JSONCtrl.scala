package controllers

import model.PCMAPIUtils
import org.opencompare.api.java.impl.io.KMFJSONExporter
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Result, AnyContent, Request}


/**
 * Created by gbecan on 9/21/15.
 */
class JSONCtrl extends IOCtrl {

  val jsonExporter = new KMFJSONExporter()

  val outputParametersForm = Form(
    mapping(
      "file" -> text
    )(JSONExportParameters.apply)(JSONExportParameters.unapply)
  )

  override def importPCMs(implicit request: Request[AnyContent]): Result = {
    NotFound("JSON import is not implemented yet")
  }

  override def exportPCM(implicit request: Request[AnyContent]): Result = {
    val parameters = outputParametersForm.bindFromRequest.get
    val pcmJSON = Json.parse(parameters.pcm)

    val container = PCMAPIUtils.createContainers(pcmJSON).head

    val json = jsonExporter.export(container)

    Ok(json)
  }

  case class JSONExportParameters(pcm : String)
}
