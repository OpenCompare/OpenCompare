package controllers

import java.io.IOException

import model.Database
import org.opencompare.api.java.PCMFactory
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.CSVLoader
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{Action, Controller}

import scala.collection.JavaConversions._
import scala.io.Source

/**
 * Created by gbecan on 8/18/15.
 */
class IOCsv extends Controller {

  private val pcmFactory : PCMFactory = new PCMFactoryImpl()

  val form = Form(
    mapping(
      "productAsLines" -> boolean,
      "title" -> nonEmptyText,
      "separator" -> nonEmptyText(1, 1),
      "quote" -> nonEmptyText(1, 1)
    )(CSVImportParameters.apply)(CSVImportParameters.unapply)
  )

  def importPCMs() = Action { implicit request =>
    // Parse parameters
    val parameters = form.bindFromRequest.get
    val separator = parameters.separator.charAt(0)
    val quote = parameters.quote.charAt(0)

    // Read input file
    val file = request.body.asMultipartFormData.get.file("file").get
    val csvData = Source.fromFile(file.ref.file).getLines().mkString("\n")

    try {

      val loader: CSVLoader = new CSVLoader(pcmFactory, separator, quote, parameters.productAsLines)
      val pcmContainers = loader.load(csvData).toList
      val pcmContainer = pcmContainers.head
      pcmContainer.getPcm.setName(parameters.title)

      // Normalize the matrices
      for (pcmContainer <- pcmContainers) {
        pcmContainer.getPcm.normalize(pcmFactory)
      }

      // Create PCM in database
      val id = Database.INSTANCE.create(pcmContainers.head)

      // Serialize result
      val jsonResult = Database.INSTANCE.serializePCMContainersToJSON(pcmContainers)
      Ok(jsonResult)

    } catch {
      case e : IOException => BadRequest("This file is invalid")
    }

  }

}

case class CSVImportParameters(
  productAsLines : Boolean,
  title : String,
  separator : String,
  quote : String
)