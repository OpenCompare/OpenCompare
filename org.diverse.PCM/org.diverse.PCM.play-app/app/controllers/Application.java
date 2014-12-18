package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import model.Database;
import model.PCMInfo;
import model.PCMVariable;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.export.PCMtoJson;
import org.diverse.pcm.api.java.impl.export.PCMtoJsonImpl;
import org.diverse.pcm.api.java.impl.io.JSONLoaderImpl;
import org.diverse.pcm.api.java.io.JSONLoader;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {
    
    public static Result index() {
        return ok(views.html.index.render());
    }

    public static Result list() {
        List<PCMInfo> pcms = Database.INSTANCE.list();
        return ok(views.html.list.render(pcms));
    }

    public static Result search(String request) {

        // TODO : find PCMs named "request" or with a product named "request"
        List<PCMVariable> results = Database.INSTANCE.search(request);

        return ok(views.html.search.render(request, results));
    }


    public static Result view(String id) {
        return ok(views.html.view.render(id));
    }

    public static Result get(String id) {
        PCM pcm = Database.INSTANCE.get(id).getPcm();
        PCMtoJson serializer = new PCMtoJsonImpl();
        String json = serializer.toJson(pcm);
        return ok(json);
    }

    public static Result save(String id) {
        System.out.println("id = " + id);
        String json = request().body().asJson().toString();
        Database.INSTANCE.update(id, json);
        return ok();
    }

    public static Result edit(String id) {
        return ok(views.html.edit.render(id));
    }

}
