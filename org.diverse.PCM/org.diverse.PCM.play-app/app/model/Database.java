package model;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.export.PCMtoJson;
import org.diverse.pcm.api.java.impl.PCMFactoryImpl;
import org.diverse.pcm.api.java.impl.export.PCMtoJsonImpl;
import org.diverse.pcm.api.java.impl.io.JSONLoaderImpl;
import org.diverse.pcm.api.java.io.JSONLoader;

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
    private JSONLoader loader = new JSONLoaderImpl();

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


    public List<PCMVariable> search(String request) {
        DBCollection pcms = db.getCollection("pcms");
        DBObject query = new BasicDBObject("$text", new BasicDBObject("$search", "\"" + request + "\""));
        DBCursor cursor = pcms.find(query);

        List<PCMVariable> results = new ArrayList<PCMVariable>();
        for (DBObject result : cursor) {
            results.add(createPCMVariable(result));
        }

        return results;
    }


    public List<String> list() {
        DBCollection pcms = db.getCollection("pcms");

        DBCursor cursor = pcms.find();
        List<String> results = new ArrayList<String>();

        for (DBObject result : cursor) {
            PCM pcm = createPCMVariable(result).getPcm();
            results.add(pcm.getName());
        }

        return results;
    }

    public void save(PCM pcm) {
        DBCollection pcms = db.getCollection("pcms");
        PCMtoJson serializer = new PCMtoJsonImpl();
        String json = serializer.toJson(pcm);
        pcms.insert((DBObject) JSON.parse(json));

    }


    public PCMVariable get(String id) {
        DBCollection pcms = db.getCollection("pcms");
        DBObject searchById = new BasicDBObject("_id", new ObjectId(id));
        DBObject result = pcms.findOne(searchById);

        PCMVariable var = createPCMVariable(result);

        return var;
    }


    private PCMVariable createPCMVariable(DBObject object) {
        String id = object.removeField("_id").toString();
        String json = JSON.serialize(object);
        PCM pcm = loader.load(json);
        PCMVariable var = new PCMVariable(id, pcm);
        return var;
    }
}
