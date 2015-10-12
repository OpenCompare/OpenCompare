package controllers

import javax.inject.{Singleton, Inject}
import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models._
import models.daos.PCMContainerDAO
import org.opencompare.api.java.{PCMContainer, PCMFactory}
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.{KMFJSONLoader, KMFJSONExporter}
import org.opencompare.api.java.io.{HTMLExporter, CSVExporter}
import org.opencompare.formalizer.extractor.CellContentInterpreter
import org.opencompare.io.wikipedia.io.{WikiTextLoader, WikiTextTemplateProcessor, MediaWikiAPI, WikiTextExporter}
import org.opencompare.io.wikipedia.parser.CellContentExtractor
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

import collection.JavaConversions._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
 * Created by gbecan on 08/01/15.
 * Updated by smangin on 21/05/15
 */
@Singleton
class PCMAPI @Inject() (
                         val messagesApi: MessagesApi,
                         val env: Environment[User, CookieAuthenticator],
                         val pcmContainerDAO: PCMContainerDAO,
                         val pcmAPIUtils : PCMAPIUtils
                         ) extends BaseController {

    private val pcmFactory : PCMFactory = new PCMFactoryImpl()
    private val mediaWikiAPI : MediaWikiAPI = new MediaWikiAPI("wikipedia.org")
    private val wikitextTemplateProcessor : WikiTextTemplateProcessor= new WikiTextTemplateProcessor(mediaWikiAPI)
    private val miner : WikiTextLoader= new WikiTextLoader(wikitextTemplateProcessor)
    private val cellContentInterpreter : CellContentInterpreter = new CellContentInterpreter()


    def get(id : String) = Action.async {
      val result = pcmContainerDAO.get(id)
      result flatMap { dbPCM =>
        if (dbPCM.isDefined) {
          val futureJson = pcmAPIUtils.serializePCMContainer(dbPCM.get.pcmContainer.get)
          futureJson map { json =>
            Ok(json).withHeaders(
              "Access-Control-Allow-Origin" -> "*"
            )
          }
        } else {
          Future.successful(NotFound(id))
        }
      }
    }

    def save(id : String) = Action { request =>
        val json = request.body.asJson.get

        val ipAddress = request.remoteAddress; // TODO : For future work !

        val pcmContainers = pcmAPIUtils.parsePCMContainers(json)

        if (pcmContainers.size == 1) {
            val databasePCM = new DatabasePCM(Some(id), Some(pcmContainers.head))
            Database.update(databasePCM)
            Ok("")
        } else {
            BadRequest("multiple pcms not supported")
        }
    }

    def create() = Action { request =>
        val json = request.body.asJson.get
        val pcmContainers = pcmAPIUtils.parsePCMContainers(json)
        if (pcmContainers.size == 1) {
            val id = Database.create(pcmContainers.get(0))
            Ok(id)
        } else {
            BadRequest("multiple pcms not supported")
        }

    }

    def remove(id : String) = SecuredAction(AdminAuthorization()) {
        Database.remove(id)
        Ok("")
    }


    def extractContent = Action { request =>
        val json = request.body.asJson.get.as[JsObject]
        val pcmType = json.value.get("type")
        val rawContent = json.value.get("rawContent")

        if (pcmType.isDefined && rawContent.isDefined) {

            val pcmTypeString = pcmType.get.as[JsString].value
            val rawContentString = rawContent.get.as[JsString].value

            if (pcmTypeString == "wikipedia") {
                val language = "en"
                val wikitextContentExtractor = new CellContentExtractor(language, miner.preprocessor, wikitextTemplateProcessor, miner.parser)
                val content = wikitextContentExtractor.extractCellContent(rawContentString)
                Ok(content)
            } else {
                BadRequest("unknown type")
            }
        } else {
          BadRequest("type and content must be defined")
        }
    }

    def search(searchedString : String) = Action {
        val results = Database.search(searchedString).toList

        val jsonResults = JsArray(results.map(result =>
            JsObject(Seq(
                "id" -> JsString(result.id),
                "name" -> JsString(result.name)
            ))
        ))

        Ok(jsonResults)
    }

}
