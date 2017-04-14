package pcm_Export_Mongo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.bson.Document;

import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

import JSONformating.PCMtoJSON;
import JSONformating.model.JSONFormat;
import pcm_InfoContainer.*;

public class Main {

	public static String inputpath = ""; // give path with argv

	public static int total = 0;
	public static int count = 0;

	public static void main(String[] args) throws IOException {

		// inputpath = args[0];

		inputpath = "output114/";
		//inputpath = "input-pcm/";
		//inputpath = "../../New_Model/output114/";

		try {
			MongoClient mongoClient = new MongoClient();
			MongoCollection<Document> collection =
			mongoClient.getDatabase("opencompare").getCollection("pcms");

			Stream<Path> paths = Files.walk(Paths.get(inputpath));

			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".pcm")) {
					System.out.println("> PCM read from " + filePath);

					File pcmFile = new File(filePath.toString());

					PCMLoader loader = new KMFJSONLoader();
					List<PCMContainer> pcmContainers = null;
					try {
						pcmContainers = loader.load(pcmFile);
					} catch (Exception e) {
						e.printStackTrace();
					}

					for (PCMContainer pcmContainer : pcmContainers) {
						total++;
						
						PCMInfoContainer pcmic = null;
						pcmContainer.getPcm().setName("" + filePath.toString() + "" + total);

						try {
							pcmic = new PCMInfoContainer(pcmContainer);
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (pcmic != null && pcmic.isProductChartable()) {

							JSONFormat json = null;
							// Export to mongoDB database
							try {
								json = PCMtoJSON.mkNewJSONFormatFromPCM(pcmContainer);
							} catch (java.lang.NullPointerException | IOException e) {
								e.printStackTrace();
							}

							if (json != null) {
								String pcmString = new KMFJSONExporter().export(pcmContainer); // json.export();
								
								try {
									Document doc = Document.parse(pcmString);
									collection.insertOne(doc);
									System.out.println("> PCM exported to Database");
									count++;
								} catch (org.bson.json.JsonParseException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			});
			System.out.println(count + "/" + total + " PCMs exported");
			//mongoClient.close();
			paths.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
