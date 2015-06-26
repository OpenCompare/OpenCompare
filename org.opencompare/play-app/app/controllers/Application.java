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


    @Inject
    private I18nService i18nService;

    public Result index() {
        return ok(views.html.index.render(i18nService));
    }

    public Result list(int limit, int page) {
        List<PCMInfo> pcms = Database.INSTANCE.list(limit, page);
        int count = (int) Database.INSTANCE.count();
        int nbPages = count / limit;
        if (count % limit != 0) {
            nbPages++;
        }
        return ok(views.html.list.render(i18nService, pcms, limit, page, nbPages));
    }

    public Result search(String request) {

        // TODO : find PCMs named "request" or with a product named "request"
        List<DatabasePCM> results = Database.INSTANCE.search(request);

        return ok(views.html.search.render(i18nService, request, results));
    }


    public Result view(String id) {
        DatabasePCM var = Database.INSTANCE.get(id);

        if (var.hasIdentifier()) {
            return ok(views.html.view.render(i18nService, var.getId(), var.getPCMContainer().getPcm()));
        } else {
            return ok(views.html.edit.render(i18nService, null, null, null));
        }

    }

    public Result edit(String id) {
        boolean exists = Database.INSTANCE.exists(id);
        if (exists) {
            return ok(views.html.edit.render(i18nService, id, null, null));
        } else {
            return ok(views.html.edit.render(i18nService, null, null, null));
        }

    }

    public Result create() {
        return ok(views.html.edit.render(i18nService, null, null, null));
    }

    public Result importer(String ext) {
        if (ext.equals("csv")) {
            return ok(views.html.edit.render(i18nService, null, null, "CsvImport"));
        } else if (ext.equals("wikipedia")) {
            return ok(views.html.edit.render(i18nService, null, null, "WikipediaImport"));
        }
        return notFound();
    }

    public Result embedPCM(String id) {
        boolean exists = Database.INSTANCE.exists(id);
        if (exists) {
            return ok(views.html.embed.render(i18nService, id, null, null));
        } else {
            return ok(views.html.embed.render(i18nService, null, null, null));
        }

    }

    public Result embed() {
        return ok(views.html.embed.render(i18nService, null, null, null));
    }

}
