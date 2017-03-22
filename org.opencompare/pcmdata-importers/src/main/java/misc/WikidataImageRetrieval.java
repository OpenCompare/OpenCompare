package misc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WikidataImageRetrieval {
	
	
	// propertyID: either P18 or P154
	public String retrieve(String entityID, String propertyID) throws Exception {
		
		// first step
		String wikiDataReq = "https://www.wikidata.org/w/api.php?action=wbgetclaims&entity=" + entityID + "&property=" + propertyID + "&format=json";
		
		// (2) request for getting the image name
		String imageName = processWikidataRequest(wikiDataReq, entityID, propertyID);
		
		// TODO
		// encode imageName (blank spaces)
		String encodedImageName = _encode (imageName);
		
	
		// (3) Wikimedia 
		String wikimediaReq = "https://commons.wikimedia.org/w/api.php?action=query&prop=imageinfo&iiprop=url&titles=File:" + encodedImageName + "&format=json";
		
		// (4) get URL
		return getImageURLFromWikimedia(wikimediaReq);
		
	}

	private String _encode(String imageName) {
		return imageName.replaceAll(" ", "%20");
	}

	private String getImageURLFromWikimedia(String wikimediaReq) throws Exception {
		
		 URL url = new URL(wikimediaReq);
	     HttpURLConnection request = (HttpURLConnection) url.openConnection();
	     request.connect();

	    // Convert to a JSON object to print data
	    JsonParser jp = new JsonParser(); //from gson
	    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
	    JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
	    
	    
	    // WEIRD
	    // TODO
	    String jsonStr = rootobj.toString();
	    String startingUrl = jsonStr.substring(jsonStr.indexOf("{\"url\":") + 8);
	    
	    return startingUrl.substring(0, startingUrl.indexOf("\""));
	    
		
	}

	private String processWikidataRequest(String wikiDataReq, String entityID, String propertyID) throws Exception {
		 
		    URL url = new URL(wikiDataReq);
		    HttpURLConnection request = (HttpURLConnection) url.openConnection();
		    request.connect();

		    // Convert to a JSON object to print data
		    JsonParser jp = new JsonParser(); //from gson
		    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
		    JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
		   
		    JsonObject claims = rootobj.get("claims").getAsJsonObject();
		    JsonArray property = claims.get(propertyID).getAsJsonArray();
		    JsonElement mainask = property.get(0).getAsJsonObject().get("mainsnak");
		    JsonObject dataValue = mainask.getAsJsonObject().get("datavalue").getAsJsonObject();
		   
		return dataValue.get("value").getAsString();
	}

}
