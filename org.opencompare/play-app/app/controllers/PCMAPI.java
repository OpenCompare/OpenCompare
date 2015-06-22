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
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.collection.Map;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static scala.collection.JavaConversions.seqAsJavaList;


/**
 * Created by gbecan on 08/01/15.
 * Updated by smangin on 21/05/15
 */
public class PCMAPI extends Controller {

    private static final PCMFactory pcmFactory = new PCMFactoryImpl();
    private static final KMFJSONExporter jsonExporter = new KMFJSONExporter();
    private static final CSVExporter csvExporter = new CSVExporter();
    private static final KMFJSONLoader jsonLoader = new KMFJSONLoader();
    private static final WikiTextExporter wikiExporter = new WikiTextExporter(true);
    private static final MediaWikiAPI mediaWikiAPI = new MediaWikiAPI("wikipedia.org");
    private static final WikiTextTemplateProcessor wikitextTemplateProcessor = new WikiTextTemplateProcessor(mediaWikiAPI);
    private static final WikiTextLoader miner = new WikiTextLoader(wikitextTemplateProcessor);

    private static List<PCMContainer> loadWikitext(String title){
        String language = "en";
        // Parse article from Wikipedia
        String code = mediaWikiAPI.getWikitextFromTitle(language, title);
        List<PCMContainer> pcmContainers = miner.mine(language, code, title);
        return pcmContainers; // TODO: manage several matrices case inside the page
    }

    private static List<PCMContainer> loadCsv(File fileContent, char separator, char quote, boolean productAsLines) throws IOException {
        CSVLoader loader = new CSVLoader(pcmFactory, separator, quote, productAsLines);
        List<PCMContainer> pcmContainers = loader.load(fileContent);
        return pcmContainers; // FIXME : should test size of list
    }

    private static List<PCMContainer> loadCsv(String fileContent, char separator, char quote, boolean productAsLines) throws IOException {
        CSVLoader loader = new CSVLoader(pcmFactory, separator, quote, productAsLines);
        List<PCMContainer> pcmContainers = loader.load(fileContent);
        return pcmContainers; // FIXME : should test size of list
    }

    public static Result get(String id) {
        DatabasePCM dbPCM = Database.INSTANCE.get(id);
        String json = Database.INSTANCE.serializeDatabasePCM(dbPCM);
        return ok(json);
    }

    public static Result save(String id) {
        String json = request().body().asJson().toString();

        String ipAddress = request().remoteAddress(); // TODO : For future work !

        Database.INSTANCE.update(id, json);
        return ok();
    }

    public static Result create() {
        String json = request().body().asJson().toString();
        String id = Database.INSTANCE.create(json);
        return ok(id);
    }

    public static Result remove(String id) {
        Database.INSTANCE.remove(id);
        return ok();
    }

    public static Result convert(String id, String type, boolean productAsLines) {
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

    public static Result importer(String type) {
        PCMContainer pcmContainer;

        // Getting form values
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String title = dynamicForm.get("title");
        Boolean productAsLines = false;
        if (dynamicForm.get("productAsLines") != null) {
            productAsLines = true;
        }

        JsValue data = null;

        if (type.equals("wikipedia")) {

            try {
                List<PCMContainer> pcmContainers = PCMAPI.loadWikitext(title);
                pcmContainer = pcmContainers.get(0);
                data = Json.parse(jsonExporter.export(pcmContainer));
            } catch (Exception e) {
                return notFound("The page '" + title + "' has not been found or is empty."); // TODO: manage the different kind of exceptions
            }

        } else if (type.equals("csv")) {
            // Options
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
                List<PCMContainer> pcmContainers = PCMAPI.loadCsv(fileContent, separator, quote, productAsLines);
                pcmContainer = pcmContainers.get(0);
                data = Json.parse(jsonExporter.export(pcmContainer));
            } catch (IOException e) {
                return badRequest("This file is invalid."); // TODO: manage the different kind of exceptions
            }
            pcmContainer.getPcm().setName(title);

        } else {
            return internalServerError("File format not found or invalid.");
        }

        // Normalizing and validating the matrix, just in case
        pcmContainer.getPcm().normalize(pcmFactory);
        //if (!pcm.isValid()) { FIXME: does not work ??
        //    return internalServerError("This matrix is not valid !");
        //}

        String jsonResult = Database.INSTANCE.serializePCMContainerToJSON(pcmContainer);
        return ok(jsonResult);
    }

    /*
    Parse the json file and generate a container
     */
    private static List<PCMContainer> createContainers(JsObject jsonContent) {
        String jsonPCM = Json.stringify(jsonContent.value().apply("pcm"));
        List<PCMContainer> containers = jsonLoader.load(jsonPCM);
        JsObject jsonMetadata = (JsObject) jsonContent.value().apply("metadata");
        for (PCMContainer container : containers) {
            saveMetadatas(container, jsonMetadata);
        }
        return containers;
    }

    /*
    Insert metadatas inside the container based on the json metadatas
     */
    private static void saveMetadatas(PCMContainer container, JsObject jsonMetadata) {
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

    public static Result exporter(String type) {
        String code;

        // Getting form values
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String title = dynamicForm.get("title");
        Boolean productAsLines = false;
        if (dynamicForm.get("productAsLines").equals("true")) {
            productAsLines = true;
        }
        JsObject jsonContent = (JsObject) Json.parse(dynamicForm.field("file").value());
        PCMContainer container = createContainers(jsonContent).get(0);
        container.getMetadata().setProductAsLines(productAsLines);

        if (type.equals("wikitext")) {

            code = wikiExporter.export(container);

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

    public static Result extractContent() {
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
}
