package JSONformating.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.Scanner;

import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import JSONformating.model.JBooleanValue;
import JSONformating.model.JCell;
import JSONformating.model.JFeature;
import JSONformating.model.JMultipleValue;
import JSONformating.model.JNumberValue;
import JSONformating.model.JProduct;
import JSONformating.model.JSONFormat;
import JSONformating.model.JSONFormatType;
import JSONformating.model.JStringValue;
import JSONformating.model.JValue;

public class JSONReader {

	public static JSONFormat importJSON(String filename) throws IOException{
		try {
			Scanner scanner = new Scanner(new File(filename));
			String json = scanner.useDelimiter("\\Z").next();
			scanner.close();
			return importJSONString(json);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	
		return null;
	}
	
	public static JSONFormat importJSONString(String json) throws IOException{
		JsonElement jelement = new JsonParser().parse(json);
		return createJSONFormat(jelement);
	}

	private static JSONFormat createJSONFormat(JsonElement jelement) throws IOException {
		JFeature feature;
		JProduct product;
		JCell cell;
		JsonObject jo, jC, features, obj, products, cells;
		JSONFormat jf = new JSONFormat();
		obj = jelement.getAsJsonObject();
		jf.setName(obj.get("name").getAsString());
		jf.setCreator(obj.get("creator").getAsString());
		jf.setLicense(obj.get("license").getAsString());
		jf.setSource(obj.get("source").getAsString());
		jf.setPrimaryFeatureID(obj.get("primaryFeatureID").getAsString());
		features = obj.get("features").getAsJsonObject();
		
		for(Entry<String, JsonElement> e : features.entrySet()){
			jo = e.getValue().getAsJsonObject();
			feature = new JFeature();
			feature.setId(jo.get("id").getAsString());
			feature.setName(jo.get("name").getAsString());
			feature.setType(JSONFormatType.getType(jo.get("type").getAsString()));
			jf.addFeature(feature);
		}
		products = obj.get("products").getAsJsonObject();
		for(Entry<String, JsonElement> eP : products.entrySet()){
			jo = eP.getValue().getAsJsonObject();
			product = new JProduct();
			product.setId(eP.getKey());
			cells = jo.get("cells").getAsJsonObject();
			for(Entry<String, JsonElement> eC : cells.entrySet()){
				jC = eC.getValue().getAsJsonObject();
				cell = new JCell();
				cell.setId(eC.getKey());
				cell.setType(JSONFormatType.getType(jC.get("type").getAsString()));
				cell.setFeatureID(jC.get("featureID").getAsString());
				cell.setProductID(jC.get("productID").getAsString());
//				cell.setPartial(jC.get("isPartial").getAsBoolean());
//				cell.setUnit(jC.get("unit").getAsString());
				cell.setValue(getJValue(jC));
				product.addCell(cell);
			}
			jf.addProduct(product);
		}
		return jf;
	}
	
	public static JValue getJValue(JsonObject cell) throws IOException{
		JSONFormatType type = JSONFormatType.getType(cell.get("type").getAsString());
		JsonElement value = cell.get("value");
		switch(type){
		case BOOLEAN:
			JBooleanValue boolValue = new JBooleanValue();
			boolValue.setValue(value.getAsBoolean());
			return boolValue;
		case DATE:case IMAGE:case STRING:case URL:case VERSION:
			JStringValue stringValue = new JStringValue();
			stringValue.setValue(value.getAsString());
			return stringValue;
		case INTEGER:case REAL:
			JNumberValue numValue = new JNumberValue();
			numValue.setValue(value.getAsDouble());
			return numValue;
		case MULTIPLE:
			JMultipleValue mulValue = new JMultipleValue();
			JsonArray array = value.getAsJsonArray();
			for(JsonElement j : array){
				if(j instanceof JsonNull){
					System.out.println("JsonNull in JSONReader.getJValue(...)");
					System.out.println(value.toString());
				}else{
					mulValue.addValue(getJValueForMultiple(j));
				}
			}
			return mulValue;
		case UNDEFINED:
		default:
			return null;
		}
	}

	private static JValue getJValueForMultiple(JsonElement j) {
		try{
			Double d = j.getAsDouble();
			JNumberValue numberValue = new JNumberValue();
			numberValue.setValue(d);
			return numberValue;
		}catch(java.lang.NumberFormatException e){
			String str = j.getAsString();
			if(str.equals("true") || str.equals("false")){
				JBooleanValue booleanValue = new JBooleanValue();
				booleanValue.setValue(j.getAsBoolean());
				return booleanValue;
			}else{
				JStringValue stringValue = new JStringValue();
				stringValue.setValue(str);
				return stringValue;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		String filename = "off_output/pcms/en_french-blue-veined-cheeses.new.pcm";
		Scanner scanner = new Scanner(new File(filename));
		String in = scanner.useDelimiter("\\Z").next();
		scanner.close();
		JSONFormat jf = importJSON(filename);
		String out = jf.export();
		System.out.println(in.equals(out));
//		System.out.println(in);
//		System.out.println(out);
	}

}
