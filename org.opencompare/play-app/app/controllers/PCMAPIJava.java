package controllers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import model.Database;
import model.PCMAPIUtils;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.CSVExporter;
import org.opencompare.api.java.io.CSVLoader;
import org.opencompare.api.java.io.HTMLExporter;
import org.opencompare.api.java.io.HTMLLoader;
import org.opencompare.formalizer.extractor.CellContentInterpreter;
import org.opencompare.io.wikipedia.io.MediaWikiAPI;
import org.opencompare.io.wikipedia.io.WikiTextExporter;
import org.opencompare.io.wikipedia.io.WikiTextLoader;
import org.opencompare.io.wikipedia.io.WikiTextTemplateProcessor;
import play.api.libs.json.*;
import play.data.DynamicForm;
import play.data.Form;
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
 * Created by gbecan on 8/18/15.
 */
@Singleton
public class PCMAPIJava extends Controller {

    private final PCMFactory pcmFactory = new PCMFactoryImpl();
    private final MediaWikiAPI mediaWikiAPI = new MediaWikiAPI("wikipedia.org");
    private final WikiTextTemplateProcessor wikitextTemplateProcessor = new WikiTextTemplateProcessor(mediaWikiAPI);
    private final WikiTextLoader miner = new WikiTextLoader(wikitextTemplateProcessor);
    private final CellContentInterpreter cellContentInterpreter = new CellContentInterpreter();

    @Inject
    private I18nService i18nService;

    private List<PCMContainer> loadHtml(String fileContent, boolean productAsLines) throws IOException {

        HTMLLoader loader = new HTMLLoader(pcmFactory, productAsLines);
        List<PCMContainer> pcmContainers = loader.load(fileContent);
        return pcmContainers; // FIXME : should test size of list
    }

    public Result embedFromHTML() {
        List<PCMContainer> pcmContainers;

        // Getting form values
        DynamicForm dynamicForm = Form.form().bindFromRequest();

        Boolean productAsLines = false;
        if (dynamicForm.get("productAsLines") != null) {
            productAsLines = true;
        }

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
            if (pcmContainers.isEmpty()) {
                return notFound("No matrices were found in this html page");
            }
        } catch (IOException e) {
            return badRequest("This file is invalid."); // TODO: manage the different kind of exceptions
        }
        // Normalize the matrices
        for (PCMContainer pcmContainer : pcmContainers) {
            pcmContainer.getPcm().normalize(pcmFactory);
        }
        String id = Database.INSTANCE.create(pcmContainers.get(0));

        return ok(id);
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
