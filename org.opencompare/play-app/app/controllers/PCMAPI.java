package controllers;

import model.Database;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.io.CSVExporter;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by gbecan on 08/01/15.
 */
public class PCMAPI extends Controller {

    public static Result get(String id) {
        PCM pcm = Database.INSTANCE.get(id).getPcm();
        KMFJSONExporter serializer = new KMFJSONExporter();
        String json = serializer.export(pcm);
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
        if (ext == "json") {
            KMFJSONExporter serializer = new KMFJSONExporter();
            String json = serializer.export(pcm);
            data = json;
        } else if (ext == "csv") {
            CSVExporter serializer = new CSVExporter();
            String csv = serializer.export(pcm);
            data = csv;
        } else {
            return badRequest("Extension type error. Return only 'csv' and 'json' format !");
        }
        return ok(data);
    }
}
