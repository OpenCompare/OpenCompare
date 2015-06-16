package controllers;

import model.Database;
import model.PCMInfo;
import model.PCMVariable;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

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
            return ok(views.html.edit.render(null, null, null));
        }

    }

    public static Result edit(String id) {
        boolean exists = Database.INSTANCE.exists(id);
        if (exists) {
            return ok(views.html.edit.render(id, null, null));
        } else {
            return ok(views.html.edit.render(null, null, null));
        }

    }

    public static Result create() {
        return ok(views.html.edit.render(null, null, null));
    }

    public static Result importer(String ext) {
        if (ext.equals("csv")) {
            return ok(views.html.edit.render(null, null, "CsvImport"));
        } else if (ext.equals("wikipedia")) {
            return ok(views.html.edit.render(null, null, "WikipediaImport"));
        }
        return notFound();
    }

}
