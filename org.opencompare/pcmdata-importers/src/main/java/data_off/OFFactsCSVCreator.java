package data_off;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.bson.Document;
import org.json.JSONException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.opencsv.CSVWriter;

public class OFFactsCSVCreator {

	/*
	 *  ps -ef | grep mongo
	 */

	public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	public static boolean MAX_ON = false;
	public static int MAX_PRODUCTS = 100;
	private MongoClient mongo;
	private MongoCollection<Document> collection;

	public OFFactsCSVCreator(){

		mongo = new MongoClient();
		collection = mongo.getDatabase("off").getCollection("products");
		System.out.println(collection.count() + " products in database");

	}

	public MongoCursor<Document> getMongoCursorForCategory(String category){
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("categories_tags", category);
		FindIterable<Document> test = collection.find(whereQuery);
		MongoCursor<Document> cursor = test.iterator();
		return cursor;
	}

	public void createCSVFromCategory(String category, boolean getImageUrl) throws IOException, JSONException{
		MongoCursor<Document> cursor = getMongoCursorForCategory(category);
		MongoCursor<Document> cursorForHeader = getMongoCursorForCategory(category);
		if(category.contains(":")){
			category = category.replace(':', '_');
		}
		OFFToProduct.setGetImageUrl(getImageUrl);
		int count = createCSVFromMongoCursor(category, cursor, cursorForHeader);

		System.out.println(count + " products in the category " + category);
	}

	public static int createCSVFromMongoCursor(String fileName, MongoCursor<Document> cursor, MongoCursor<Document> cursorForHeader) throws JSONException{
		System.out.println("Writing to file " + fileName);
//		DateFormat dateFormat = new SimpleDateFormat("-ddMMyyyy-HHmmss");
//		Date date = new Date();
		String newFileName = "off_output/" + fileName /* + dateFormat.format(date)*/ + ".csv";
		File file = new File(newFileName);

		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int count = 0;
		try (Writer writer = new BufferedWriter(new FileWriter(file))) {

			CSVWriter csvwriter = new CSVWriter(writer, ';', '\"');
			List<String> nutriments = getNutrimentsList(cursorForHeader);
			String[] header  = makeHeader(nutriments);
//			System.out.println(header.length);
			csvwriter.writeNext(header);//writing the header
			Document product;
			while(cursor.hasNext() && (MAX_ON ?count < MAX_PRODUCTS:true)){
				product = cursor.next();
				csvwriter.writeNext(OFFToProduct.mkOFFProductStrings(OFFToProduct.mkOFFProductFromBSON(product), nutriments, header.length));
				count++;
				if(count%1000 == 0){
					System.out.println(count + " products written");
				}

			}
			csvwriter.close();
			return count;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("OK WTF");

		}
		return count;
	}


	private static String[] makeHeader(List<String> nutriments) {
		List<String> header = new ArrayList<String>();
		header.add("id");
		header.add("product_name");
		header.add("countries");
		header.add("ingredients");
		header.add("brands");
		header.add("stores");
		header.addAll(nutriments);
		header.add("image_url");
		String[] a = new String[1];
		return header.toArray(a);
	}

	private static List<String> getNutrimentsList(MongoCursor<Document> cursorForHeader) {
		Map<String, Integer> nutriments = new HashMap<String, Integer>();
		Document product;
		Set<Entry<String, Object>> nutrimentsSet;
		String key;
		while(cursorForHeader.hasNext()){
			product = cursorForHeader.next();
			nutrimentsSet = ((Document) product.get("nutriments")).entrySet();
			for(Entry<String, Object> entry : nutrimentsSet){
				key = entry.getKey();
				if(key.endsWith("_value") && !key.equals("energy_value")){
					key = key.substring(0, key.length()-6);
					if(!nutriments.containsKey(key)){
						nutriments.put(key, 1); //adding the nutriment to the map if it is not already in
					}else{
						nutriments.put(key, nutriments.get(key) + 1); //incrementing
					}
				}else if(key.equals("energy_100g")){
					if(!nutriments.containsKey(key)){
						nutriments.put(key, 1); //adding the nutriment to the map if it is not already in
					}else{
						nutriments.put(key, nutriments.get(key) + 1); //incrementing
					}
				}
			}
		}

		return filterNMostUsedNutriments(nutriments, 10);
	}

	private static List<String> filterNMostUsedNutriments(Map<String, Integer> nutriments, Integer N) {
		List<String> list = new ArrayList<String>();
		while(list.size() < N){
			int max = 0;
			String key = null;
			for(Entry<String, Integer> entry : nutriments.entrySet()){
				if(entry.getValue() > max){
					key = entry.getKey();
					max = entry.getValue();
				}
			}
			list.add(key);
			nutriments.remove(key);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getCategoriesWithBetween(int min, int max){
		long total = collection.count();
		FindIterable<Document> test = collection.find();
		MongoCursor<Document> cursor = test.iterator();
		Document product;
		List<String> catList;
		Map<String, Integer> catMap = new HashMap<String, Integer>();
		int count = 0;
		int ratio = 0;
		int ratioDelta = 10;
		System.out.println("Checking categories...");
		while(cursor.hasNext()){
			product = cursor.next();
			catList = (ArrayList<String>) product.get("categories_tags");
			if(catList != null){
				for(String cat : catList){
					if(catMap.containsKey(cat)){
						catMap.put(cat, catMap.get(cat)+1);
					}else{
						catMap.put(cat, 1);
					}
				}
			}
			count++;
			//			if(count%1000 == 0){
			//				System.out.println(count + " products checked");
			//			}
			if(count*100/total>=ratio){
				System.out.println(ratio + "% of products checked");
				ratio += ratioDelta;
			}
		}
		catMap.remove("");
		System.out.println(catMap.size() + " categories in total");
		String key;
		Map<String, Integer> temp = new HashMap<String, Integer>(catMap);
		for(Map.Entry<String, Integer> entry : temp.entrySet()){
			if(entry.getValue() < min || entry.getValue() > max){
				key = entry.getKey();
				catMap.remove(key);
			}
		}

		System.out.println(catMap.size() + " categories with between " + min + " and " + max + " products");
		return catMap.keySet();
	}


	public void close(){
		mongo.close();
	}

}
