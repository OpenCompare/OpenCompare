package model;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by gbecan on 11/12/14.
 */
public class Database {

    public static Database INSTANCE = new Database();
    private KMFJSONLoader kmfLoader = new KMFJSONLoader();
    private KMFJSONExporter kmfSerializer = new KMFJSONExporter();
    private Base64.Decoder base64Decoder = Base64.getDecoder();

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
        DBCursor cursor = pcms.find(new BasicDBObject()) // new BasicDBObject("name", "1") // TODO : optimize request
                .sort(new BasicDBObject("_id", 1))
                .skip(skipped)
                .limit(limit);

        List<PCMInfo> results = new ArrayList<PCMInfo>();

        for (DBObject result : cursor) {
            DBObject dbPCM = (DBObject) result.get("pcm");
            Object dbID = result.get("_id");

            if (dbID != null && dbPCM != null) {
                String id = dbID.toString();
                String name = dbPCM.get("name").toString();
                name = new String(base64Decoder.decode(name.getBytes())); // Decode Base64 characters
                PCMInfo info = new PCMInfo(id, name);
                results.add(info);
            }

        }

        return results;
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

    public void update(DatabasePCM databasePCM) {
        DBObject dbPCMContainer = serializePCMContainer(databasePCM.getPCMContainer());
        pcms.update(new BasicDBObject("_id", new ObjectId(databasePCM.getId())), dbPCMContainer);
    }

    public String create(PCMContainer pcmContainer) {
        DBObject newPCM = serializePCMContainer(pcmContainer);
        WriteResult result = pcms.insert(newPCM);
        String id = newPCM.get("_id").toString();
        return id;
    }

    private DatabasePCM createDatabasePCMInstance(DBObject object) {
        DatabasePCM var;
        if (object == null) {
            var = new DatabasePCM(null,null);
        } else {
            String id = object.get("_id").toString();

            String json = JSON.serialize(object.get("pcm"));
            List<PCMContainer> pcmContainers = kmfLoader.load(json);
            if (pcmContainers.size() == 1) {
                PCMContainer pcmContainer = pcmContainers.get(0);
                PCMMetadata metadata = pcmContainer.getMetadata();

                // Load metadata
                DBObject dbMetadata = (DBObject) object.get("metadata");

                // Load product positions
                List<DBObject> dbProductPositions = (List<DBObject>) dbMetadata.get("productPositions");
                for (DBObject dbProductPosition : dbProductPositions) {
                    String productName = dbProductPosition.get("product").toString();
                    Product product = null;
                    for (Product p : pcmContainer.getPcm().getProducts()) {
                        if (p.getName().equals(productName)) {
                            product = p;
                            break;
                        }
                    }
                    int position = Integer.parseInt(dbProductPosition.get("position").toString());
                    metadata.setProductPosition(product, position);
                }

                // Load feature positions
                List<DBObject> dbFeaturePositions = (List<DBObject>) dbMetadata.get("featurePositions");
                for (DBObject dbFeaturePosition : dbFeaturePositions) {
                    String featureName = dbFeaturePosition.get("feature").toString();
                    Feature feature = null;
                    for (Feature f : pcmContainer.getPcm().getConcreteFeatures()) {
                        if (f.getName().equals(featureName)) {
                            feature = f;
                            break;
                        }
                    }
                    int position = Integer.parseInt(dbFeaturePosition.get("position").toString());
                    metadata.setFeaturePosition(feature, position);
                }

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

    private DBObject serializePCMContainer(PCMContainer pcmContainer) {
        PCM pcm = pcmContainer.getPcm();
        PCMMetadata metadata = pcmContainer.getMetadata();

        // Serialize PCM
        String pcmInJSON = kmfSerializer.export(pcmContainer);

        DBObject dbPCM = (DBObject) JSON.parse(pcmInJSON);

        // Serialize metadata
        DBObject dbMetadata = new BasicDBObject();

        // Serialize product positions
        List<DBObject> dbProductPositions = new ArrayList<DBObject>();
        for (Product product : pcm.getProducts()) {
            DBObject dbProductPosition = new BasicDBObject();
            dbProductPosition.put("product", product.getName());
            dbProductPosition.put("position", metadata.getProductPosition(product));
            dbProductPositions.add(dbProductPosition);
        }
        dbMetadata.put("productPositions", dbProductPositions);

        // Serialize feature positions
        List<DBObject> dbFeaturePositions = new ArrayList<DBObject>();
        for (Feature feature : pcm.getConcreteFeatures()) {
            DBObject dbFeaturePosition = new BasicDBObject();
            dbFeaturePosition.put("feature", feature.getName());
            dbFeaturePosition.put("position", metadata.getFeaturePosition(feature));
            dbFeaturePositions.add(dbFeaturePosition);
        }
        dbMetadata.put("featurePositions", dbFeaturePositions);

        // Encapsulate the PCM and its metadata in a object
        DBObject dbContainer = new BasicDBObject();
        dbContainer.put("pcm", dbPCM);
        dbContainer.put("metadata", dbMetadata);

        return dbContainer;
    }

    public String serializeDatabasePCM(DatabasePCM dbPCM) {
        DBObject dbContainer = serializePCMContainer(dbPCM.getPCMContainer());
        return JSON.serialize(dbContainer);
    }

    public String serializePCMContainersToJSON(List<PCMContainer> pcmContainers) {
        List<DBObject> dbContainers = new ArrayList<>();

        for (PCMContainer pcmContainer : pcmContainers) {
            DBObject dbContainer = serializePCMContainer(pcmContainer);
            dbContainers.add(dbContainer);
        }

        return JSON.serialize(dbContainers);
    }

}
