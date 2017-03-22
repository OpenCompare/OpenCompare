package data_off;

import java.io.IOException;

import org.json.JSONException;

public class Run2 {

public static void main(String[] arg0) throws IOException, JSONException{
		
		OFFactsCSVCreator creator = new OFFactsCSVCreator();
		
//		creator.createCSVFromCategory("en:beers", false);
//		OFFPCMCreator.mkPCMFromCategory("en:beers");
		creator.createCSVFromCategory("en:breaded-products", true);
		OFFPCMCreator.mkPCMFromCategory("en:breaded-products");
		OFFPCMCreator.mkNewPCMFromCategory("en:breaded-products");
//		creator.createCSVFromCategory("en:seeds", true);
//		OFFPCMCreator.mkPCMFromCategory("en:seeds");
		
		OFFStats.printStats();
		
		creator.close();
	}

}
