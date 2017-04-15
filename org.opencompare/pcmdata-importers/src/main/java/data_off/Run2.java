package data_off;

import java.io.IOException;

import org.json.JSONException;

public class Run2 {

public static void main(String[] arg0) throws IOException, JSONException{
		
		OFFactsCSVCreator creator = new OFFactsCSVCreator();
		String category = "en:beers";
//		creator.createCSVFromCategory("en:beers", false);
//		OFFPCMCreator.mkPCMFromCategory("en:beers");
		creator.createCSVFromCategory(category, true);
		OFFPCMCreator.mkPCMFromCategory(category);
		OFFPCMCreator.mkNewPCMFromCategory(category);
//		creator.createCSVFromCategory("en:seeds", true);
//		OFFPCMCreator.mkPCMFromCategory("en:seeds");
		
		OFFStats.printStats();
		
		creator.close();
	}

}
