package controllers.io

import controllers._
import models.Database
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.{PCMContainer, PCMFactory}
import org.opencompare.formalizer.extractor.CellContentInterpreter
import play.api.Logger
import play.api.mvc._

/**
 * Created by gbecan on 8/19/15.
 */
abstract class IOCtrl(val name : String) extends BaseController {

  private val pcmFactory : PCMFactory = new PCMFactoryImpl()
  private val cellContentInterpreter: CellContentInterpreter = new CellContentInterpreter

  def importPCMsAction(format: String) = UserAwareAction { implicit request =>

    val resultFormat = format.replaceAll("'", "").replaceAll("\"", "") match {
      case "json" => JsonFormat()
      case "page" => PageFormat()
      case "embed" => EmbedFormat()
      case _ =>
        println(format)
        JsonFormat()
    }

    val result = importPCMs(resultFormat)

    if (result.header.status == 200) {
      Database.incrementUsage(name)
    } else {
      Database.createError(name, request)
    }

    result
  }

  def exportPCMAction = Action { request =>
    exportPCM(request)
  }

  def importPCMs(format : ResultFormat)(implicit request: Request[AnyContent], viewContext: ViewContext) : Result

  def exportPCM(implicit request : Request[AnyContent]) : Result

  /**
   * Normalize, interpret and serialize PCM
   * @param pcmContainers
   * @return PCM containers in JSON
   */
  def postprocessContainers(pcmContainers : List[PCMContainer]) : String = {

    normalizeContainers(pcmContainers)

    // Serialize result
    val jsonResult: String = Database.serializePCMContainersToJSON(pcmContainers)

    jsonResult
  }

  def normalizeContainers(pcmContainers : List[PCMContainer]) {
    for (pcmContainer <- pcmContainers) {
      val pcm = pcmContainer.getPcm
      pcm.normalize(pcmFactory)
      cellContentInterpreter.interpretCells(pcm)
    }
  }

}
