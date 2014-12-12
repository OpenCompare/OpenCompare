package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import model.Database;
import org.diverse.pcm.api.java.PCM;
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
        List<String> pcms = Database.INSTANCE.list();


        ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
        for (String pcm : pcms) {
            result.add(pcm);
        }

        return ok(result);
    }

    public static Result search(String request) {

        // TODO : find PCMs named "request" or with a product named "request"
        List<String> results = Database.INSTANCE.search(request);

        JSONLoader loader = new JSONLoaderImpl();
        List<PCM> pcms = new ArrayList<PCM>();
        for (String result : results) {
            PCM pcm = loader.load(result);
            pcms.add(pcm);
        }

        return ok(views.html.search.render(request, pcms));
    }
    
}
