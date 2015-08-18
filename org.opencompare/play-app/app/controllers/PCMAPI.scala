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

    private val pcmFactory : PCMFactory = new PCMFactoryImpl()
    private val jsonExporter : KMFJSONExporter= new KMFJSONExporter()
    private val csvExporter : CSVExporter= new CSVExporter()
    private val htmlExporter : HTMLExporter = new HTMLExporter()
    private val jsonLoader : KMFJSONLoader= new KMFJSONLoader()
    private val wikiExporter : WikiTextExporter = new WikiTextExporter(true)
    private val mediaWikiAPI : MediaWikiAPI = new MediaWikiAPI("wikipedia.org")
    private val wikitextTemplateProcessor : WikiTextTemplateProcessor= new WikiTextTemplateProcessor(mediaWikiAPI)
    private val miner : WikiTextLoader= new WikiTextLoader(wikitextTemplateProcessor)
    private val cellContentInterpreter : CellContentInterpreter = new CellContentInterpreter()

//    private List<PCMContainer> loadWikitext(String language, String title){
//        // Parse article from Wikipedia
//        String code = mediaWikiAPI.getWikitextFromTitle(language, title);
//
//        List<PCMContainer> pcmContainers = miner.mine(language, code, title);
//        for (PCMContainer pcmContainer : pcmContainers) {
//            PCM pcm = pcmContainer.getPcm();
//            pcm.normalize(pcmFactory);
//            cellContentInterpreter.interpretCells(pcm);
//        }
//        return pcmContainers;
//    }
//
//    private List<PCMContainer> loadCsv(File fileContent, char separator, char quote, boolean productAsLines) throws IOException {
//        CSVLoader loader = new CSVLoader(pcmFactory, separator, quote, productAsLines);
//        List<PCMContainer> pcmContainers = loader.load(fileContent);
//        return pcmContainers; // FIXME : should test size of list
//    }
//
//    private List<PCMContainer> loadCsv(String fileContent, char separator, char quote, boolean productAsLines) throws IOException {
//        CSVLoader loader = new CSVLoader(pcmFactory, separator, quote, productAsLines);
//        List<PCMContainer> pcmContainers = loader.load(fileContent);
//        return pcmContainers; // FIXME : should test size of list
//    }
//
//    private List<PCMContainer> loadHtml(String fileContent, boolean productAsLines) throws IOException {
//
//        HTMLLoader loader = new HTMLLoader(pcmFactory, productAsLines);
//        List<PCMContainer> pcmContainers = loader.load(fileContent);
//        return pcmContainers; // FIXME : should test size of list
//    }
//
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

//    public Result convert(String id, String type, boolean productAsLines) {
//        DatabasePCM dbPCM = Database.INSTANCE.get(id);
//        PCMContainer pcmContainer = dbPCM.getPCMContainer();
//        pcmContainer.getMetadata().setProductAsLines(productAsLines);
//        String data;
//        if (type.equals("csv")) {
//            data = csvExporter.export(pcmContainer);
//        } else if (type.equals("wikitext")) {
//            data = wikiExporter.export(pcmContainer);
//        } else {
//            return badRequest("Type error. Return only 'csv' or 'wikitext' types.");
//        }
//        return ok(data);
//    }
//
//    public Result importer(String type) {
//        List<PCMContainer> pcmContainers;
//
//        // Getting form values
//        DynamicForm dynamicForm = Form.form().bindFromRequest();
//
//        Boolean productAsLines = false;
//        if (dynamicForm.get("productAsLines") != null) {
//            productAsLines = true;
//        }
//
//        if (type.equals("wikipedia")) {
//            String url = dynamicForm.get("url");
//            try {
//                URL pageURL = new URL(url);
//
//                String host = pageURL.getHost();
//                String language = host.substring(0, host.indexOf('.'));
//                String file = URLDecoder.decode(pageURL.getFile(), StandardCharsets.UTF_8.name());
//
//                if (file.endsWith("/")) {
//                    file = file.substring(0, file.length() - 1);
//                }
//                String title = file.substring(file.lastIndexOf('/') + 1);
//
//                pcmContainers = loadWikitext(language, title);
//                if (pcmContainers.isEmpty()) {
//                    return notFound("No matrices were found in this Wikipedia page");
//                }
//            } catch(MalformedURLException e) {
//                return notFound("URL is not a valid Wikipedia page");
//            } catch (Exception e) {
//                e.printStackTrace();
//                return notFound("The page has not been found."); // TODO: manage the different kind of exceptions
//            }
//
//        } else if (type.equals("csv")) {
//            // Options
//            String title = dynamicForm.get("title");
//            String fileContent = "";
//            try {
//                Http.MultipartFormData body = request().body().asMultipartFormData();
//                Http.MultipartFormData.FilePart file = body.getFile("file");
//                fileContent = Files.toString(file.getFile(), Charsets.UTF_8);
//            } catch (Exception e) {
//                fileContent = dynamicForm.field("file").value();
//            }
//
//            char separator = dynamicForm.get("separator").charAt(0);
//            char quote = '"';
//            String delimiter = dynamicForm.get("quote");
//            if (delimiter.length() != 0) {
//                quote = delimiter.charAt(0);
//            }
//            try {
//                pcmContainers = loadCsv(fileContent, separator, quote, productAsLines);
//                PCMContainer pcmContainer = pcmContainers.get(0);
//                pcmContainer.getPcm().setName(title);
//            } catch (IOException e) {
//                return badRequest("This file is invalid."); // TODO: manage the different kind of exceptions
//            }
//        } else if (type.equals("html")) {
//            // Options
//            String title = dynamicForm.get("title");
//            String fileContent = "";
//            try {
//                Http.MultipartFormData body = request().body().asMultipartFormData();
//                Http.MultipartFormData.FilePart file = body.getFile("file");
//                fileContent = Files.toString(file.getFile(), Charsets.UTF_8);
//            } catch (Exception e) {
//                fileContent = dynamicForm.field("file").value();
//            }
//
//            try {
//                pcmContainers = loadHtml(fileContent, productAsLines);
//                PCMContainer pcmContainer = pcmContainers.get(0);
//                pcmContainer.getPcm().setName(title);
//                if (pcmContainers.isEmpty()) {
//                    return notFound("No matrices were found in this html page");
//                }
//            } catch (IOException e) {
//                return badRequest("This file is invalid."); // TODO: manage the different kind of exceptions
//            }
//
//
//        } else {
//            return internalServerError("File format not found or invalid.");
//        }
//
//        // Normalize the matrices
//        for (PCMContainer pcmContainer : pcmContainers) {
//            pcmContainer.getPcm().normalize(pcmFactory);
//        }
//        String id = Database.INSTANCE.create(pcmContainers.get(0));
//        System.out.print(id);
//        // Serialize result
//        String jsonResult = Database.INSTANCE.serializePCMContainersToJSON(pcmContainers);
//        return ok(jsonResult);
//    }

//    public Result embedFromHTML() {
//        List<PCMContainer> pcmContainers;
//
//        // Getting form values
//        DynamicForm dynamicForm = Form.form().bindFromRequest();
//
//        Boolean productAsLines = false;
//        if (dynamicForm.get("productAsLines") != null) {
//            productAsLines = true;
//        }
//
//        // Options
//        String title = dynamicForm.get("title");
//        String fileContent = "";
//        try {
//            Http.MultipartFormData body = request().body().asMultipartFormData();
//            Http.MultipartFormData.FilePart file = body.getFile("file");
//            fileContent = Files.toString(file.getFile(), Charsets.UTF_8);
//        } catch (Exception e) {
//            fileContent = dynamicForm.field("file").value();
//        }
//
//        try {
//            pcmContainers = loadHtml(fileContent, productAsLines);
//            if (pcmContainers.isEmpty()) {
//                return notFound("No matrices were found in this html page");
//            }
//        } catch (IOException e) {
//            return badRequest("This file is invalid."); // TODO: manage the different kind of exceptions
//        }
//        // Normalize the matrices
//        for (PCMContainer pcmContainer : pcmContainers) {
//            pcmContainer.getPcm().normalize(pcmFactory);
//        }
//        String id = Database.INSTANCE.create(pcmContainers.get(0));
//
//        return ok(id);
//    }

    /*
      Parse the json file and generate a container
     */
    def createContainers(jsonContent : JsValue) : List[PCMContainer] = {
        val jsonObject = jsonContent.as[JsObject] // FIXME : check converstion to scala
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

//    public Result exporter(String type) {
//        String code;
//
//        // Getting form values
//        DynamicForm dynamicForm = Form.form().bindFromRequest();
//        String title = dynamicForm.get("title");
//        Boolean productAsLines = false;
//
//        if (dynamicForm.get("productAsLines").equals("true")) {
//            productAsLines = true;
//        }
//        JsValue jsonContent = Json.parse(dynamicForm.field("file").value());
//        PCMContainer container = createContainers(jsonContent).get(0);
//        container.getMetadata().setProductAsLines(productAsLines);
//
//        if (type.equals("wikitext")) {
//
//            code = wikiExporter.export(container);
//
//} else if (type.equals("html")) {
//
//            code = htmlExporter.export(container);
//
//        } else if (type.equals("csv")) {
//
//            char separator = dynamicForm.get("separator").charAt(0);
//            char quote = '\u0000'; // null char hack
//            String delimiter = dynamicForm.get("quote");
//            if (delimiter.length() != 0) {
//                quote = delimiter.charAt(0);
//            }
//            code = csvExporter.export(container, separator, quote);
//
//        } else {
//            return internalServerError("File format not found or invalid.");
//        }
//        return ok(code);
//    }

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

    def i18n = Action {
//        val code = lang.code
//        Ok(Json.stringify(i18nService.getMessagesJson(code)))
      Ok("") // FIXME : hack
    }

//    public Result setLang(String language) {
//        if (i18nService.isDefined(language)) {
//            changeLang(language.toUpperCase());
//            return ok("");
//        } else {
//            clearLang();
//            return ok("language unknown");
//        }
//    }

}
