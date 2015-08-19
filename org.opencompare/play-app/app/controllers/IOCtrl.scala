package controllers

import model.Database
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.{PCMContainer, PCMFactory}
import org.opencompare.formalizer.extractor.CellContentInterpreter
import play.api.mvc._

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 8/19/15.
 */
trait IOCtrl extends Controller {

  private val pcmFactory : PCMFactory = new PCMFactoryImpl()
  private val cellContentInterpreter: CellContentInterpreter = new CellContentInterpreter

  def importPCMsAction = Action { request =>
    importPCMs(request)
  }

  def exportPCMAction = Action { request =>
    exportPCM(request)
  }

  def importPCMs(implicit request : Request[AnyContent]) : Result

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
