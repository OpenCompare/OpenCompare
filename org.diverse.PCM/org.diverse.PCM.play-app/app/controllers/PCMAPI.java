package controllers;

import model.Database;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.export.PCMtoJson;
import org.diverse.pcm.api.java.impl.export.PCMtoJsonImpl;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by gbecan on 08/01/15.
 */
public class PCMAPI extends Controller {

    public static Result get(String id) {
        PCM pcm = Database.INSTANCE.get(id).getPcm();
        PCMtoJson serializer = new PCMtoJsonImpl();
        String json = serializer.toJson(pcm);
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

}
