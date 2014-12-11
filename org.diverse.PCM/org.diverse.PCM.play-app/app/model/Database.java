package model;

import com.mongodb.*;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by gbecan on 11/12/14.
 */
public class Database {

    public static Database INSTANCE = new Database();

    private DB db;

    private Database() {
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            this.db = mongoClient.getDB("opencompare");

            db.getCollection("pcms").createIndex(new BasicDBObject("name", "text"));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public List<String> search(String request) {
        DBCollection pcms = db.getCollection("pcms");
        DBObject query = new BasicDBObject("$text", new BasicDBObject("$search", "\"" + request + "\""));
        DBCursor cursor = pcms.find(query);

        List<String> results = new ArrayList<String>();
        for (DBObject result : cursor) {
            String json = JSON.serialize(result);
            results.add(json);
        }

        return results;
    }


    public List<String> list() {
        DBCollection pcms = db.getCollection("pcms");

        DBCursor cursor = pcms.find();
        List<String> results = new ArrayList<String>();
        for (DBObject result : cursor) {
            String json = JSON.serialize(result);
            results.add(json);
        }

        return results;
    }



}
