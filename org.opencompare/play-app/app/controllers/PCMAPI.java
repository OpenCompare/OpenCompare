package controllers;

import model.Database;
import org.opencompare.api.java.PCM;
import org.opencompare.io.wikipedia.WikipediaPageMiner;
import org.opencompare.io.wikipedia.pcm.Page;
import org.opencompare.io.wikipedia.export.PCMModelExporter;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.CSVExporter;
import org.opencompare.api.java.io.CSVLoader;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

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

        return pcms.get(0);
    }

    private static PCM loadCsv(String fileContent, char separator, char quote){
        CSVLoader loader = new CSVLoader(pcmFactory, separator, quote);
        //try {
            PCM pcm = loader.load(fileContent);
        //} catch (IndexOutOfBoundsException e) {
        //    return internalServerError("Index out of bound exception : " + e.getLocalizedMessage());
        //
        //}
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

    public static Result convertById(String id, String ext) {
        PCM pcm = Database.INSTANCE.get(id).getPcm();
        String data;
        if (ext.equals("json")) {
            data = jsonExporter.export(pcm);
        } else if (ext.equals("csv")) {
            data = csvExporter.export(pcm);
        } else {
            return badRequest("Extension type error. Return only 'csv' and 'json' format !");
        }
        return ok(data);
    }

    public static Result convertByFile(String type) {
        PCM pcm;

        // Getting form values
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        Boolean productAsLines = Boolean.valueOf(dynamicForm.get("productAsLines"));
        String title = dynamicForm.get("title");

        if (type.equals("wikipedia")) {

            pcm = PCMAPI.loadWikitext(title);

        } else if (type.equals("csv")) {
            // Options
            String fileContent = dynamicForm.get("fileContent");
            char separator = dynamicForm.get("separator").charAt(0);
            char quote = '"';
            String delimiter = dynamicForm.get("quote");
            if (delimiter.length() != 0) {
                quote = delimiter.charAt(0);
            }

            pcm = PCMAPI.loadCsv(fileContent, separator, quote);
            pcm.setName(title);
            // Reversing the matrix if specified
            if (!productAsLines) {
                pcm.invert(pcmFactory);
            }

        } else {
            return internalServerError("Type not found or invalid.");
        }

        // Normalizing and validating the matrix, just in case
        pcm.normalize(pcmFactory);
        //if (!pcm.isValid()) {
        //    return internalServerError("This matrix is not valid !");
        //}

        // TODO : bad idea to redirect to a page in this API.
        //return ok(jsonExporter.toJson(pcm));
        return ok(views.html.edit.render(null, pcm));
    }

}
