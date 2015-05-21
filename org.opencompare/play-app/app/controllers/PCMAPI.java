package controllers;

import model.Database;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.io.CSVExporter;
import org.opencompare.api.java.io.CSVLoader;
import org.opencompare.api.java.io.PCMLoader;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by gbecan on 08/01/15.
 * Updated by smangin on 21/05/15
 */
public class PCMAPI extends Controller {

    private static final KMFJSONExporter jsonSerializer = new KMFJSONExporter();
    private static final CSVExporter csvSerializer = new CSVExporter();

    public static Result get(String id) {
        PCM pcm = Database.INSTANCE.get(id).getPcm();
        String json = jsonSerializer.export(pcm);
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

    public static Result convert(String id, String ext) {
        String data = null;

        PCM pcm = Database.INSTANCE.get(id).getPcm();
        if (ext.equals("json")) {
            String json = jsonSerializer.export(pcm);
            data = json;
        } else if (ext.equals("csv")) {
            String csv = csvSerializer.export(pcm);
            data = csv;
        } else {
            return badRequest("Extension type error. Return only 'csv' and 'json' format !");
        }
        return ok(data);
    }

    public static Result importer() {
        PCMFactory factory = new PCMFactoryImpl();
        PCM pcm;
        String id;

        // Getting form values
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String type = dynamicForm.get("type");
        Boolean productAsLines = Boolean.valueOf(dynamicForm.get("productAsLines"));
        String fileContent = dynamicForm.get("fileContent");

        if (type.equals("json")) {

            // TODO : create an implementation of api-java.io.PCMLoader
            return internalServerError("Json importation has not been implemented yet !");

        } else if (type.equals("csv")) {

            // Specific CSV options
            char separator = dynamicForm.get("separator").charAt(0);
            char quote = '"';
            String delimiter = dynamicForm.get("quote");
            if (delimiter.length() != 0) {
                quote = delimiter.charAt(0);
            }
            Boolean reverse = Boolean.valueOf(dynamicForm.get("reverse"));

            // Effective load
            CSVLoader loader = new CSVLoader(factory, separator, quote, productAsLines);
            try {
                pcm = loader.load(fileContent);
            } catch (IndexOutOfBoundsException e) {
                return internalServerError("Index out of bound exception : " + e.getLocalizedMessage());
            }
            pcm.setName(dynamicForm.get("title"));

        } else {
            return internalServerError("Type not found or invalid. (json|csv)");
        }

        // Reversing the matrix if specified
        if (productAsLines) {
            pcm.invert(factory);
        }
        // Normalizing and validating the matrix, just in case
        pcm.normalize(factory);
        //if (!pcm.isValid()) {
        //    return internalServerError("This matrix is not valid !");
        //}

        // Finally creating the resource
        id = Database.INSTANCE.create(jsonSerializer.export(pcm));
        return ok(id);
    }
}
