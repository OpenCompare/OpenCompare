package data_off;

import java.io.IOException;
import java.util.Set;

import org.bson.Document;
import org.json.JSONException;

import com.mongodb.client.MongoCursor;

public class Run {

	public static void main(String[] arg0) throws IOException, JSONException{

		OFFactsCSVCreator creator = new OFFactsCSVCreator();

		Set<String> cat = creator.getCategoriesWithBetween(900, 1000);
		
		int max = cat.size();

		int count = 0;
		for(String s : cat){
			count++;
			System.out.println(s + " " + count + " / " + max);
			creator.createCSVFromCategory(s, true);
			OFFPCMCreator.mkPCMFromCategory(s);
			OFFPCMCreator.mkNewPCMFromCategory(s);

		}

		OFFStats.printStats();

		creator.close();

	}
}
