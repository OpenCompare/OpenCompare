package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import model.Database;
import play.mvc.Controller;
import play.mvc.Result;

import java.net.UnknownHostException;
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

        return ok(views.html.search.render(request, results));
    }
    
}
