package model;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbecan on 11/12/14.
 */
public class Database {

    public static Database INSTANCE = new Database();
    private KMFJSONLoader loader = new KMFJSONLoader();
    private KMFJSONExporter serializer = new KMFJSONExporter();

    private DB db;
    private DBCollection pcms;
    private String mongoVersion;

    private Database() {
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            this.db = mongoClient.getDB("opencompare");
            mongoVersion = db.command("buildInfo").getString("version");

            pcms = db.getCollection("pcms");
            boolean indexInitialized = false;
            for (DBObject indexInfo : pcms.getIndexInfo()) {
                if (indexInfo.get("name").equals("name_text")) {
                    indexInitialized = true;
                }
            }

            if (!indexInitialized) {
                pcms.createIndex(new BasicDBObject("name", "text"));
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public List<DatabasePCM> search(String request) {

        List<DatabasePCM> results = new ArrayList<DatabasePCM>();

        // $search operator requires mongo >= 2.6
        DBObject query = new BasicDBObject("$text", new BasicDBObject("$search", "\"" + request + "\""));
        DBCursor cursor = pcms.find(query);

        for (DBObject result : cursor) {
            results.add(createDatabasePCMInstance(result));
        }

        // Code snippet for text search with mongo < 2.6
//        DBObject query = new BasicDBObject();
//        query.put("text", pcms.getName());
//        query.put("search", request);
//        CommandResult commandResult = db.command(query);

        return results;
    }

    public long count() {
        return pcms.count();
    }

    public List<PCMInfo> list(int limit, int page) {
        int skipped = (page - 1) * limit;
        DBCursor cursor = pcms.find(new BasicDBObject(), new BasicDBObject("name", "1"))
                .sort(new BasicDBObject("_id",1))
                .skip(skipped)
                .limit(limit);

        List<PCMInfo> results = new ArrayList<PCMInfo>();

        for (DBObject result : cursor) {
            String id = result.get("_id").toString();
            String name = result.get("name").toString();
            PCMInfo info = new PCMInfo(id, name);
            results.add(info);
        }

        return results;
    }

    public void save(PCMContainer pcm) {
        String json = serializer.export(pcm);
        pcms.insert((DBObject) JSON.parse(json));
        // TODO : save PCM metadata
    }


    public DatabasePCM get(String id) {
        if (ObjectId.isValid(id)) {
            DBObject searchById = new BasicDBObject("_id", new ObjectId(id));
            DBObject result = pcms.findOne(searchById);

            DatabasePCM var = createDatabasePCMInstance(result);

            return var;
        } else {
            return new DatabasePCM(null, null);
        }
    }

    public void update(String id, String json) {
        pcms.update(new BasicDBObject("_id", new ObjectId(id)), (DBObject) JSON.parse(json));
    }

    public String create(String json) {
        // TODO : create metadata
        DBObject newPCM = (DBObject) JSON.parse(json);
        WriteResult result = pcms.insert(newPCM);
        String id = newPCM.get("_id").toString();
        return id;
    }

    private DatabasePCM createDatabasePCMInstance(DBObject object) {
        DatabasePCM var;
        if (object == null) {
            var = new DatabasePCM(null,null);
        } else {
            String id = object.removeField("_id").toString();
            String json = JSON.serialize(object);
            List<PCMContainer> pcmContainers = loader.load(json);
            if (pcmContainers.size() == 1) {
                PCMContainer pcmContainer = pcmContainers.get(0);
                // TODO : load metadata
                var = new DatabasePCM(id, pcmContainer);
            } else {
                var = new DatabasePCM(null,null);
            }

        }
        return var;
    }

    public boolean exists(String id) {
        if (ObjectId.isValid(id)) {
            DBObject searchById = new BasicDBObject("_id", new ObjectId(id));
            DBObject result = pcms.findOne(searchById);
            return result != null;
        } else {
            return false;
        }
    }

    public void remove(String id) {
        pcms.remove(new BasicDBObject("_id", new ObjectId(id)));
    }
}
