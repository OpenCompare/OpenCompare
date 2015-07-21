package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import model.Database;
import model.DatabasePCM;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.CSVExporter;
import org.opencompare.api.java.io.CSVLoader;
import org.opencompare.io.wikipedia.io.MediaWikiAPI;
import org.opencompare.io.wikipedia.io.WikiTextExporter;
import org.opencompare.io.wikipedia.io.WikiTextLoader;
import org.opencompare.io.wikipedia.io.WikiTextTemplateProcessor;
import org.opencompare.io.wikipedia.parser.CellContentExtractor;
import play.api.libs.json.*;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.collection.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static scala.collection.JavaConversions.seqAsJavaList;


/**
 * Created by gbecan on 08/01/15.
 * Updated by smangin on 21/05/15
 */
@Singleton
public class PCMAPI extends Controller {

    private final PCMFactory pcmFactory = new PCMFactoryImpl();
    private final KMFJSONExporter jsonExporter = new KMFJSONExporter();
    private final CSVExporter csvExporter = new CSVExporter();
    private final HTMLExporter htmlExporter = new HTMLExporter();
    private final KMFJSONLoader jsonLoader = new KMFJSONLoader();
    private final WikiTextExporter wikiExporter = new WikiTextExporter(true);
    private final MediaWikiAPI mediaWikiAPI = new MediaWikiAPI("wikipedia.org");
    private final WikiTextTemplateProcessor wikitextTemplateProcessor = new WikiTextTemplateProcessor(mediaWikiAPI);
    private final WikiTextLoader miner = new WikiTextLoader(wikitextTemplateProcessor);

    @Inject
    private I18nService i18nService;

    private List<PCMContainer> loadWikitext(String language, String title){
        // Parse article from Wikipedia
        String code = mediaWikiAPI.getWikitextFromTitle(language, title);
        List<PCMContainer> pcmContainers = miner.mine(language, code, title);
        return pcmContainers; // TODO: manage several matrices case inside the page
    }

    private List<PCMContainer> loadCsv(File fileContent, char separator, char quote, boolean productAsLines) throws IOException {
        CSVLoader loader = new CSVLoader(pcmFactory, separator, quote, productAsLines);
        List<PCMContainer> pcmContainers = loader.load(fileContent);
        return pcmContainers; // FIXME : should test size of list
    }

    private List<PCMContainer> loadCsv(String fileContent, char separator, char quote, boolean productAsLines) throws IOException {
        CSVLoader loader = new CSVLoader(pcmFactory, separator, quote, productAsLines);
        List<PCMContainer> pcmContainers = loader.load(fileContent);
        return pcmContainers; // FIXME : should test size of list
    }

    private List<PCMContainer> loadHtml(String fileContent, boolean productAsLines) throws IOException {
        HTMLLoader loader = new HTMLLoader(pcmFactory, productAsLines);
        List<PCMContainer> pcmContainers = loader.load(fileContent);
        return pcmContainers; // FIXME : should test size of list
    }

    public Result get(String id) {
        DatabasePCM dbPCM = Database.INSTANCE.get(id);
        String json = Database.INSTANCE.serializeDatabasePCM(dbPCM);
        return ok(json);
    }

    public Result save(String id) {
        JsValue json = Json.parse(request().body().asJson().toString()); // TODO : optimize

        String ipAddress = request().remoteAddress(); // TODO : For future work !

        List<PCMContainer> pcmContainers = createContainers(json);

        if (pcmContainers.size() == 1) {
            DatabasePCM databasePCM = new DatabasePCM(id, pcmContainers.get(0));
            Database.INSTANCE.update(databasePCM);
            return ok();
        } else {
            return badRequest("multiple pcms not supported");
        }
    }

    public Result create() {
        JsValue json = Json.parse(request().body().asJson().toString()); // TODO : optimize
        List<PCMContainer> pcmContainers = createContainers(json);
        if (pcmContainers.size() == 1) {
            String id = Database.INSTANCE.create(pcmContainers.get(0));
            return ok(id);
        } else {
            return badRequest("multiple pcms not supported");
        }

    }

    public Result remove(String id) {
        Database.INSTANCE.remove(id);
        return ok();
    }

    public Result convert(String id, String type, boolean productAsLines) {
        DatabasePCM dbPCM = Database.INSTANCE.get(id);
        PCMContainer pcmContainer = dbPCM.getPCMContainer();
        pcmContainer.getMetadata().setProductAsLines(productAsLines);
        String data;
        if (type.equals("csv")) {
            data = csvExporter.export(pcmContainer);
        } else if (type.equals("wikitext")) {
            data = wikiExporter.export(pcmContainer);
        } else {
            return badRequest("Type error. Return only 'csv' or 'wikitext' types.");
        }
        return ok(data);
    }

    public Result importer(String type) {
        List<PCMContainer> pcmContainers;

        // Getting form values
        DynamicForm dynamicForm = Form.form().bindFromRequest();

        Boolean productAsLines = false;
        if (dynamicForm.get("productAsLines") != null) {
            productAsLines = true;
        }

        if (type.equals("wikipedia")) {
            String url = dynamicForm.get("url");
            try {
                URL pageURL = new URL(url);

                String host = pageURL.getHost();
                String language = host.substring(0, host.indexOf('.'));
                String file = URLDecoder.decode(pageURL.getFile(), StandardCharsets.UTF_8.name());

                if (file.endsWith("/")) {
                    file = file.substring(0, file.length() - 1);
                }
                String title = file.substring(file.lastIndexOf('/') + 1);

                pcmContainers = loadWikitext(language, title);
                if (pcmContainers.isEmpty()) {
                    return notFound("No matrices were found in this Wikipedia page");
                }
            } catch(MalformedURLException e) {
                return notFound("URL is not a valid Wikipedia page");
            } catch (Exception e) {
                e.printStackTrace();
                return notFound("The page has not been found."); // TODO: manage the different kind of exceptions
            }

        } else if (type.equals("csv")) {
            // Options
            String title = dynamicForm.get("title");
            String fileContent = "";
            try {
                Http.MultipartFormData body = request().body().asMultipartFormData();
                Http.MultipartFormData.FilePart file = body.getFile("file");
                fileContent = Files.toString(file.getFile(), Charsets.UTF_8);
            } catch (Exception e) {
                fileContent = dynamicForm.field("file").value();
            }

            char separator = dynamicForm.get("separator").charAt(0);
            char quote = '"';
            String delimiter = dynamicForm.get("quote");
            if (delimiter.length() != 0) {
                quote = delimiter.charAt(0);
            }
            try {
                pcmContainers = loadCsv(fileContent, separator, quote, productAsLines);
                PCMContainer pcmContainer = pcmContainers.get(0);
                pcmContainer.getPcm().setName(title);
            } catch (IOException e) {
                return badRequest("This file is invalid."); // TODO: manage the different kind of exceptions
            }
        } else if (type.equals("html")) {
            // Options
            String title = dynamicForm.get("title");
            String fileContent = "";
            try {
                Http.MultipartFormData body = request().body().asMultipartFormData();
                Http.MultipartFormData.FilePart file = body.getFile("file");
                fileContent = Files.toString(file.getFile(), Charsets.UTF_8);
            } catch (Exception e) {
                fileContent = dynamicForm.field("file").value();
            }

            try {
                pcmContainers = loadHtml(fileContent, productAsLines);
                PCMContainer pcmContainer = pcmContainers.get(0);
                pcmContainer.getPcm().setName(title);
            } catch (IOException e) {
                return badRequest("This file is invalid."); // TODO: manage the different kind of exceptions
            }


        } else {
            return internalServerError("File format not found or invalid.");
        }

        // Normalize the matrices
        for (PCMContainer pcmContainer : pcmContainers) {
            pcmContainer.getPcm().normalize(pcmFactory);
        }

        // Serialize result
        String jsonResult = Database.INSTANCE.serializePCMContainersToJSON(pcmContainers);
        return ok(jsonResult);
    }

    /*
    Parse the json file and generate a container
     */
    private List<PCMContainer> createContainers(JsValue jsonContent) {
        JsObject jsonObject = (JsObject) jsonContent;
        String jsonPCM = Json.stringify(jsonObject.value().apply("pcm"));
        List<PCMContainer> containers = jsonLoader.load(jsonPCM);
        JsObject jsonMetadata = (JsObject) jsonObject.value().apply("metadata");
        for (PCMContainer container : containers) {
            saveMetadatas(container, jsonMetadata);
        }
        return containers;
    }

    /*
    Insert metadatas inside the container based on the json metadatas
     */
    private void saveMetadatas(PCMContainer container, JsObject jsonMetadata) {
        PCMMetadata metadata = container.getMetadata();
        PCM pcm = metadata.getPcm();

        JsArray jsonProductPositions = (JsArray) jsonMetadata.value().apply("productPositions");
        JsArray jsonFeaturePositions = (JsArray) jsonMetadata.value().apply("featurePositions");

        for (JsValue jsonProductPosition : seqAsJavaList(jsonProductPositions.value())) {
            Map<String, JsValue> jsonPos = ((JsObject) jsonProductPosition).value();
            String productName = ((JsString) jsonPos.apply("product")).value();
            int position = Integer.parseInt(jsonPos.apply("position").toString());

            Product product= null;
            for (Product p : pcm.getProducts()) {
                if (p.getName().equals(productName)) { // FIXME : equals based on name breaks same name products
                    product = p;
                    break;
                }
            }
            metadata.setProductPosition(product, position);
        }

        for (JsValue jsonFeaturePosition : seqAsJavaList(jsonFeaturePositions.value())) {
            Map<String, JsValue> jsonPos = ((JsObject) jsonFeaturePosition).value();
            String featureName = ((JsString) jsonPos.apply("feature")).value();
            int position = Integer.parseInt(jsonPos.apply("position").toString());

            Feature feature = null;
            for (Feature f : pcm.getConcreteFeatures()) {
                if (f.getName().equals(featureName)) { // FIXME : equals based on name breaks same name features
                    feature = f;
                    break;
                }
            }
            metadata.setFeaturePosition(feature, position);
        }
    }

    public Result exporter(String type) {
        String code;

        // Getting form values
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String title = dynamicForm.get("title");
        Boolean productAsLines = false;

        if (dynamicForm.get("productAsLines").equals("true")) {
            productAsLines = true;
        }
        JsValue jsonContent = Json.parse(dynamicForm.field("file").value());
        PCMContainer container = createContainers(jsonContent).get(0);
        container.getMetadata().setProductAsLines(productAsLines);

        if (type.equals("wikitext")) {

            code = wikiExporter.export(container);

        } else if (type.equals("html")) {

            code = htmlExporter.export(container);

        } else if (type.equals("csv")) {

            char separator = dynamicForm.get("separator").charAt(0);
            char quote = '\u0000'; // null char hack
            String delimiter = dynamicForm.get("quote");
            if (delimiter.length() != 0) {
                quote = delimiter.charAt(0);
            }
            code = csvExporter.export(container, separator, quote);

        } else {
            return internalServerError("File format not found or invalid.");
        }
        return ok(code);
    }

    public Result extractContent() {
        JsonNode json = request().body().asJson();
        String type = json.get("type").asText();
        String rawContent = json.get("rawContent").asText();

        if (type != null && rawContent != null) {
            String content = "";
            if ("wikipedia".equals(type)) {
                String language = "en";
                CellContentExtractor wikitextContentExtractor = new CellContentExtractor(language, miner.preprocessor(), wikitextTemplateProcessor, miner.parser());
//                content = wikitextTemplateProcessor.expandTemplate(rawContent);
                content = wikitextContentExtractor.extractCellContent(rawContent);
            } else {
                return badRequest("unknown type");
            }
            return ok(content);
        }
        return badRequest();
    }

    public Result i18n() {
        return ok(i18nService.getMessagesJson(lang().code()).toString());
    }

    public Result setLang(String language) {
        if (i18nService.isDefined(language)) {
            changeLang(language.toUpperCase());
            return ok("");
        } else {
            clearLang();
            return ok("language unknown");
        }
    }

}
