package controllers;

import model.Database;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.io.wikipedia.WikipediaPageMiner;
import org.opencompare.io.wikipedia.pcm.Page;
import org.opencompare.io.wikipedia.export.PCMModelExporter;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.CSVExporter;
import org.opencompare.api.java.io.CSVLoader;
import play.Logger;
import play.api.libs.json.Json;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static scala.collection.JavaConversions.*;

/**
 * Created by gbecan on 08/01/15.
 * Updated by smangin on 21/05/15
 */
public class PCMAPI extends Controller {

    private static final PCMFactory pcmFactory = new PCMFactoryImpl();
    private static final PCMModelExporter pcmExporter = new PCMModelExporter();
    private static final KMFJSONExporter jsonExporter = new KMFJSONExporter();
    private static final CSVExporter csvExporter = new CSVExporter();
    private static final KMFJSONLoader jsonLoader = new KMFJSONLoader();

    private static PCM loadWikitext(String title){
        WikipediaPageMiner miner = new WikipediaPageMiner();

        // Parse article from Wikipedia
        String code = miner.getPageCodeFromWikipedia(title);
        String preprocessedCode = miner.preprocess(code);
        Page page = miner.parse(preprocessedCode, title);

        // PCM model export
        List<PCM> pcms = seqAsJavaList(pcmExporter.export(page));

        return pcms.get(0); // TODO: manage several matrices case inside the page
    }

    private static PCM loadCsv(File fileContent, char separator, char quote, boolean productAsLines) throws IOException {
        CSVLoader loader = new CSVLoader(pcmFactory, separator, quote, productAsLines);
        PCM pcm = loader.load(fileContent);
        return pcm;
    }

    public static Result get(String id) {
        PCM pcm = Database.INSTANCE.get(id).getPcm();
        String json = jsonExporter.export(pcm);
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

    public static Result convertById(String id, String type) {
        PCM pcm = Database.INSTANCE.get(id).getPcm();
        String data;
        if (type.equals("json")) {
            data = jsonExporter.export(pcm);
        } else if (type.equals("csv")) {
            data = csvExporter.export(pcm);
        } else {
            return badRequest("Type error. Return only 'csv' and 'json' types.");
        }
        return ok(data);
    }

    public static Result convertByFile(String type) {
        PCM pcm = new PCMImpl(null);

        // Getting form values
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String title = dynamicForm.get("title");


        if (type.equals("wikipedia")) {

            try {
                pcm = PCMAPI.loadWikitext(title);
            } catch (Exception e) {
                return internalServerError("This page has not been found or is empty."); // TODO: manage the different kind of exceptions
            }

        } else if (type.equals("csv")) {
            // Options
            //String fileContent = dynamicForm.field("file").value(); FIXME: DynamicForm does not manage multipart form data
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart fileContent = body.getFile("file");

            Boolean productAsLines = false;
            if (dynamicForm.get("productAsLines") != null) {
                productAsLines = true;
            }
            char separator = dynamicForm.get("separator").charAt(0);
            char quote = '"';
            String delimiter = dynamicForm.get("quote");
            if (delimiter.length() != 0) {
                quote = delimiter.charAt(0);
            }
            try {
                pcm = PCMAPI.loadCsv(fileContent.getFile(), separator, quote, productAsLines);
            } catch (IOException e) {
                return internalServerError("This CSV file is not well formatted."); // TODO: manage the different kind of exceptions
            }
            pcm.setName(title);

        } else {
            return internalServerError("File format not found or invalid.");
        }

        // Normalizing and validating the matrix, just in case
        pcm.normalize(pcmFactory);
        //if (!pcm.isValid()) { FIXME: does not work ??
        //    return internalServerError("This matrix is not valid !");
        //}

        // FIXME : bad idea to redirect to a page in this API.
        //return ok(jsonExporter.toJson(pcm));
        return ok(views.html.edit.render(null, Json.parse(jsonExporter.export(pcm))));
    }

}
