package controllers;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import play.mvc.Controller;
import play.mvc.Result;

import java.net.UnknownHostException;

public class Application extends Controller {
    
    public static Result index() {
        return ok(views.html.index.render());
    }

    public static Result list() {


        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            DB db = mongoClient.getDB("opencompare");
            DBCollection pcms = db.getCollection("pcms");

            System.out.println(pcms.count());

            mongoClient.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ok("[4, 5, 6]");
    }
    
}
