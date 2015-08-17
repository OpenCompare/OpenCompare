package controllers;

import model.Database;
import model.DatabasePCM;
import model.PCMInfo;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Application extends Controller {


    public Result index() {
        return ok(views.html.index.render());
    }

    public Result aboutProject() {
        return ok(views.html.aboutProject.render());
    }

    public Result aboutPrivacyPolicy() {
        return ok(views.html.aboutPrivacyPolicy.render());
    }


    public Result list(int limit, int page) {
        List<PCMInfo> pcms = Database.INSTANCE.list(limit, page);
        int count = (int) Database.INSTANCE.count();
        int nbPages = count / limit;
        if (count % limit != 0) {
            nbPages++;
        }
        return ok(views.html.list.render(pcms, limit, page, nbPages));
    }

    public Result search(String request) {

        // TODO : find PCMs named "request" or with a product named "request"
        List<PCMInfo> results = Database.INSTANCE.search(request);

        return ok(views.html.search.render(request, results));
    }


    public Result edit(String id) {
        boolean exists = Database.INSTANCE.exists(id);
        if (exists) {
            return ok(views.html.edit.render(id, null, null));
        } else {
            return ok(views.html.edit.render(null, null, null));
        }

    }

    public Result create() {
        return ok(views.html.create.render(null, null, null));
    }

    public Result importer(String ext) {
        if (ext.equals("csv")) {
            return ok(views.html.edit.render(null, null, "CsvImport"));
        } else if (ext.equals("html")) {
            return ok(views.html.edit.render(null, null, "HtmlImport"));
        } else if (ext.equals("wikipedia")) {
            return ok(views.html.edit.render(null, null, "WikipediaImport"));
        }
        return notFound();
    }

    public Result embedPCM(String id) {
        boolean exists = Database.INSTANCE.exists(id);
        if (exists) {
            return ok(views.html.embed.render(id, null, null));
        } else {
            return ok(views.html.embed.render(null, null, null));
        }

    }

    public Result embed() {
        return ok(views.html.embed.render(null, null, null));
    }

}
