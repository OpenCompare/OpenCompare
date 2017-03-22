package misc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Product;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PCMComplementerImageOfProduct {

	
	// TODO: an array of "languages" (en, fr) and an array of "properties" (P18, ...) 
	// we can basically try (brute force approach)  
	// another related TODO: allows users to configure to precisely avoid the brute force approach
	// ie user knows the language or the property (or we can infer it and pass/breath such knowledge here) 
	
	public static String LANGUAGE = "en"; // by default "en"
	
	
	
	public PCMComplementerImageOfProduct() {
	}

	/**
	 * returns a map associating feature name to image URL as found in Wikidata
	 * this map is typically used to add a new column (ft + values) and complete a PCM
	 * @param pcm
	 * @param ftName
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> completeByFeature(PCM pcm, String ftName) throws Exception {
		
		Map<String, String> ftNames2Images = new HashMap<>();
		
		List<Feature> fts = pcm.getConcreteFeatures();
		Feature ft = null;
		for (Feature feature : fts) {
			if (feature.getName().equals(ftName)) {
				ft = feature;
				break;
			}
		}
		
		if (ft == null) // not found 
			return ftNames2Images;
		
		assert(ft != null);
		
		
		
		List<Cell> cells = ft.getCells();
		for (Cell cell : cells) {
			
			String valueName = cell.getContent();
			
			// TODO compute entityID
			try {
				String entityID = computeEntityWikidataID(valueName);
							
				String propertyID = "P41"; //"P154" ; // "P18" 
				String wikidataImage = "";
				try {
					wikidataImage = new WikidataImageRetrieval().retrieve(entityID, propertyID);
				}
				catch (Exception e) {
					try {
					wikidataImage = new WikidataImageRetrieval().retrieve(entityID, "P18");
					}
					catch (Exception e1) {					
					}				
				}
				
				if (!wikidataImage.isEmpty()) {
					ftNames2Images.put(valueName, wikidataImage);
				}
				else {
					System.err.println("Image unfound for " + valueName);
				}
			}
			catch (Exception e) {
				System.err.println("Image unfound for " + valueName + " because unable to found entity");
			}
			
		}
		return ftNames2Images;
	}
	
	/**
	 * returns a map associating product name to image URL as found in Wikidata
	 * this map is typically used to add a new column (ft + values) and complete a PCM
	 * @param pcm
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> completeByProduct(PCM pcm) throws Exception {
		
		Map<String, String> pdtNames2Images = new HashMap<>();
		
		List<Product> pdts = pcm.getProducts();
		for (Product product : pdts) {
			
			String productName = product.getKeyCell().getContent();
			
			// TODO compute entityID
			try {
				String entityID = computeEntityWikidataID(productName);
							
				String propertyID = "P41"; //"P154" ; //"P18";  
				String wikidataImage = "";
				try {
					wikidataImage = new WikidataImageRetrieval().retrieve(entityID, propertyID);
				}
				catch (Exception e) {
					try {
					wikidataImage = new WikidataImageRetrieval().retrieve(entityID, "P154");
					}
					catch (Exception e1) {					
					}				
				}
				
				if (!wikidataImage.isEmpty()) {
					pdtNames2Images.put(productName, wikidataImage);
				}
				else {
					System.err.println("Image unfound for " + productName);
				}
			}
			catch (Exception e) {
				System.err.println("Image unfound for " + productName + " because unable to found entity");
			}
			
		}
		return pdtNames2Images;
	}

	private String computeEntityWikidataID(String productName) throws Exception {
		
		String wikiDataReq = "https://www.wikidata.org/w/api.php?action=wbsearchentities&search=" + productName + "&format=json&type=item&continue=0" + "&language=" + LANGUAGE;
		
		/* 
		 * disambiguation 
		 */
		
		 URL url = new URL(wikiDataReq);
	     HttpURLConnection request = (HttpURLConnection) url.openConnection();
	     request.connect();

	     // Convert to a JSON object to print data
	     JsonParser jp = new JsonParser(); //from gson
	     JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
	     JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object. 
		
	     
	     JsonArray searches = rootObj.get("search").getAsJsonArray();
	     
	     // end of disambiguation
	     // HERE we take the first one ;-) // FIXME
	     
	     JsonElement selectedEntity = searches.get(0);
	     
	   // extraction of WikidataID
	     String wikiDataID = selectedEntity.getAsJsonObject().get("id").getAsString();
	    
		 return wikiDataID;
	}

}
