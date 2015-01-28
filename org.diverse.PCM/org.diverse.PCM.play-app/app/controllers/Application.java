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

    public static Result list(int limit, int page) {
        List<PCMInfo> pcms = Database.INSTANCE.list(limit, page);
        int count = (int) Database.INSTANCE.count();
        int nbPages = count / limit;
        if (count % limit != 0) {
            nbPages++;
        }
        return ok(views.html.list.render(pcms, limit, page, nbPages));
    }

    public static Result search(String request) {

        // TODO : find PCMs named "request" or with a product named "request"
        List<PCMVariable> results = Database.INSTANCE.search(request);

        return ok(views.html.search.render(request, results));
    }


    public static Result view(String id) {
        PCMVariable var = Database.INSTANCE.get(id);

        if (var.hasIdentifier()) {
            return ok(views.html.view.render(var.getId(), var.getPcm()));
        } else {
            return ok(views.html.edit.render(null));
        }

    }

    public static Result edit(String id) {
        boolean exists = Database.INSTANCE.exists(id);
        if (exists) {
            return ok(views.html.edit.render(id));
        } else {
            return ok(views.html.edit.render(null));
        }

    }

    public static Result about() {
        return ok(views.html.about.render());
    }

    public static Result create() {
        return ok(views.html.edit.render(null));
    }

}
