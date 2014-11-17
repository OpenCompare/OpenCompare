package controllers;

import play.api.libs.json.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;

public class Application extends Controller {
    
    public static Result index() {
        return ok(views.html.index.render("OpenCompare...what else?"));
    }

    public static Result list() {
        return ok(Json.toJson(new ArrayList<String>()));
    }
    
}
