package controllers

import javax.inject.{Singleton, Inject}

import model.{PCMAPIUtils, DatabasePCM, Database}
import org.opencompare.api.java.{PCMContainer, PCMFactory}
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.{KMFJSONLoader, KMFJSONExporter}
import org.opencompare.api.java.io.{HTMLExporter, CSVExporter}
import org.opencompare.formalizer.extractor.CellContentInterpreter
import org.opencompare.io.wikipedia.io.{WikiTextLoader, WikiTextTemplateProcessor, MediaWikiAPI, WikiTextExporter}
import org.opencompare.io.wikipedia.parser.CellContentExtractor
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

import collection.JavaConversions._

/**
 * Created by gbecan on 08/01/15.
 * Updated by smangin on 21/05/15
 */
@Singleton
class PCMAPI @Inject() (val messagesApi: MessagesApi, val i18nService : I18nService) extends Controller with I18nSupport {

    private val pcmFactory : PCMFactory = new PCMFactoryImpl()
    private val csvExporter : CSVExporter= new CSVExporter()
    private val htmlExporter : HTMLExporter = new HTMLExporter()
    private val wikiExporter : WikiTextExporter = new WikiTextExporter(true)
    private val mediaWikiAPI : MediaWikiAPI = new MediaWikiAPI("wikipedia.org")
    private val wikitextTemplateProcessor : WikiTextTemplateProcessor= new WikiTextTemplateProcessor(mediaWikiAPI)
    private val miner : WikiTextLoader= new WikiTextLoader(wikitextTemplateProcessor)
    private val cellContentInterpreter : CellContentInterpreter = new CellContentInterpreter()


    def get(id : String) = Action {
        val dbPCM = Database.INSTANCE.get(id)
        val json = Database.INSTANCE.serializeDatabasePCM(dbPCM)
        Ok(json)
    }

    def save(id : String) = Action { request =>
        val json = request.body.asJson.get

        val ipAddress = request.remoteAddress; // TODO : For future work !

        val pcmContainers = PCMAPIUtils.createContainers(json)

        if (pcmContainers.size == 1) {
            val databasePCM = new DatabasePCM(id, pcmContainers.get(0))
            Database.INSTANCE.update(databasePCM)
            Ok("")
        } else {
            BadRequest("multiple pcms not supported")
        }
    }

    def create() = Action { request =>
        val json = request.body.asJson.get
        val pcmContainers = PCMAPIUtils.createContainers(json)
        if (pcmContainers.size == 1) {
            val id = Database.INSTANCE.create(pcmContainers.get(0))
            Ok(id)
        } else {
            BadRequest("multiple pcms not supported")
        }

    }

    def remove(id : String) = Action {
        Database.INSTANCE.remove(id)
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

}
