package pcm_Export_Mongo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.bson.Document;
//import org.junit.Test;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

import JSONformating.PCMtonewJSON;
import JSONformating.model.newJSONFormat;
import pcm_Export_Mongo.PCMInfoContainer;


public class Main {

	public static String inputpath = ""; // give path with argv

	public static void main(String[] args) throws IOException {

		// inputpath = args[0];
		inputpath = "input-pcm/";

		MongoClient mongoClient = new MongoClient();
		try {
			MongoCollection<Document> collection = mongoClient.getDatabase("OpenCompare").getCollection("pcms");

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
						// Get the PCM
						PCM pcm = pcmContainer.getPcm();
						PCMInfoContainer pcmic = null;

						try {
							pcmic = new PCMInfoContainer(pcm);
						} catch (Exception e) {

						}
						System.out.println("pcmic != null ? " + pcmic != null);
						if(pcmic != null)
							System.out.println("is pcmic productChartable ? " + pcmic.isProductChartable());
						if (pcmic != null && pcmic.isProductChartable()) {

							// TODO
							// Export to mongoDB database

							newJSONFormat json = PCMtonewJSON.mkNewJSONFormatFromPCM(pcmContainer);
							String pcmString = json.export();
							Document doc = Document.parse(pcmString);
							collection.insertOne(doc);
							System.out.println("> PCM exported to Database");

						}

					}

				}

			});
			mongoClient.close();
			paths.close();

		} catch (Exception e) {
			mongoClient.close();
			e.printStackTrace();
		}
	}
}
