package controllers

import javax.inject.{Singleton, Inject}

import model.{DatabasePCM, Database}
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

//import com.fasterxml.jackson.databind.JsonNode;
//import com.google.common.base.Charsets;
//import com.google.common.io.Files;
//import model.Database;
//import model.DatabasePCM;
//import play.api.libs.json.*;
//import play.data.DynamicForm;
//import play.data.Form;
//import play.mvc.Controller;
//import play.mvc.Http;
//import play.mvc.Result;
//import scala.collection.Map;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//import static scala.collection.JavaConversions.seqAsJavaList;


/**
 * Created by gbecan on 08/01/15.
 * Updated by smangin on 21/05/15
 */
@Singleton
class PCMAPI @Inject() (val messagesApi: MessagesApi, val i18nService : I18nService) extends Controller with I18nSupport {

//    private val pcmFactory : PCMFactory = new PCMFactoryImpl()
//    private val jsonExporter : KMFJSONExporter= new KMFJSONExporter()
//    private val csvExporter : CSVExporter= new CSVExporter()
//    private val htmlExporter : HTMLExporter = new HTMLExporter()
    private val jsonLoader : KMFJSONLoader= new KMFJSONLoader()
//    private val wikiExporter : WikiTextExporter = new WikiTextExporter(true)
    private val mediaWikiAPI : MediaWikiAPI = new MediaWikiAPI("wikipedia.org")
    private val wikitextTemplateProcessor : WikiTextTemplateProcessor= new WikiTextTemplateProcessor(mediaWikiAPI)
    private val miner : WikiTextLoader= new WikiTextLoader(wikitextTemplateProcessor)
//    private val cellContentInterpreter : CellContentInterpreter = new CellContentInterpreter()


    def get(id : String) = Action {
        val dbPCM = Database.INSTANCE.get(id)
        val json = Database.INSTANCE.serializeDatabasePCM(dbPCM)
        Ok(json)
    }

    def save(id : String) = Action { request =>
        val json = request.body.asJson.get

        val ipAddress = request.remoteAddress; // TODO : For future work !

        val pcmContainers = createContainers(json)

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
        val pcmContainers = createContainers(json)
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


    /*
      Parse the json file and generate a container
     */
    def createContainers(jsonContent : JsValue) : List[PCMContainer] = {
        val jsonObject = jsonContent.as[JsObject]
        val jsonPCM = Json.stringify(jsonObject.value("pcm"))
        val containers = jsonLoader.load(jsonPCM).toList
        val jsonMetadata = jsonObject.value("metadata").as[JsObject]
        for (container <- containers) {
            saveMetadatas(container, jsonMetadata)
        }
        containers
    }

    /*
      Insert metadatas inside the container based on the json metadatas
     */
    def saveMetadatas(container : PCMContainer, jsonMetadata : JsObject) {
        val metadata = container.getMetadata()
        val pcm = metadata.getPcm()

        val jsonProductPositions = jsonMetadata.value("productPositions").as[JsArray]
        val jsonFeaturePositions = jsonMetadata.value("featurePositions").as[JsArray]

        for (jsonProductPosition <- jsonProductPositions.value) {
            val jsonPos = jsonProductPosition.as[JsObject].value
            val productName = jsonPos("product").as[JsString].value
            val position = jsonPos("position").as[JsNumber].value.toIntExact

            val product = pcm.getProducts.find(_.getName == productName)  // FIXME : equals based on name breaks same name products
            if (product.isDefined) {
              metadata.setProductPosition(product.get, position)
            }

        }

        for (jsonFeaturePosition <- jsonFeaturePositions.value) {
            val jsonPos = jsonFeaturePosition.as[JsObject].value
            val featureName = jsonPos("feature").as[JsString].value
            val position = jsonPos("position").as[JsNumber].value.toIntExact

            val feature = pcm.getConcreteFeatures.find(_.getName == featureName) // FIXME : equals based on name breaks same name features
            if (feature.isDefined) {
              metadata.setFeaturePosition(feature.get, position)
            }

        }
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
