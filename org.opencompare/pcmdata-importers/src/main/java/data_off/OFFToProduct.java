package data_off;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCursor;

public class OFFToProduct {

	private static boolean GET_IMAGE_URL = false;


	public static void setGetImageUrl(boolean getImageUrl){
		GET_IMAGE_URL = getImageUrl;
	}

	public static String[] mkOFFProductStrings(OFFProduct product, List<String> nutriments, int length) throws IOException{
		String[] strArr = new String[length];
		int i = 0;
		strArr[i] = product.getId(); i++;
		strArr[i] = product.getProduct_name(); i++;
		strArr[i] = product.getCountriesString(); i++;
		strArr[i] = product.getIngredientsString(); i++;
		strArr[i] = product.getBrandsString(); i++;
		strArr[i] = product.getStoresString(); i++;

		for(String nut : nutriments){
			//			System.out.println(product.getNutrimentValue(nut));
			strArr[i] = product.getNutrimentValue(nut);
			i++;
		}
		strArr[i] = product.getImage_url();
		return strArr;
	}

	//	public static List<String[]> mkOFFProductsStrings(List<OFFProduct> products) throws IOException{
	//		List<String[]> res = new ArrayList<>();
	//		for(OFFProduct p : products){
	//			res.add(mkOFFProductStrings(p));
	//		}
	//		return res;
	//	}

	public static List<OFFProduct> mkOFFProductsFromMongoCursor(MongoCursor<Document> cursor) throws IOException, JSONException{
		List<OFFProduct> list = new ArrayList<>();
		Document product;
		int count = 0;
		String out = "+";
		while(cursor.hasNext()){
			product = cursor.next();
			list.add(mkOFFProductFromBSON(product));
			count++;
			if(count%1000 == 0){
				out += "+";
				System.out.println(count + " products done");
				System.out.println(out);
			}
		}
		System.out.println(count + " products done");
		return list;
	}

	public static OFFProduct mkOFFProductFromBSON(Document product) throws IOException, JSONException{
		OFFProduct OFFProduct = new OFFProduct();
		OFFProduct.setId(product.getString("id"));
		OFFProduct.setProduct_name(product.getString("product_name"));
		OFFProduct.setCountriesFromString(product.getString("countries"));
		OFFProduct.setIngredientsFromObject(product.get("ingredients"));
		OFFProduct.setBrandsFromString(product.getString("brands"));
		OFFProduct.setStoresFromString(product.getString("stores"));
		OFFProduct.setNutrimentsFromObject(product.get("nutriments"));
		OFFProduct.setImage_url((GET_IMAGE_URL?getImageUrl(OFFProduct.getId()):""));

		OFFStats.TOTAL_PRODUCTS++;
		return OFFProduct;
	}

	public static boolean getImageUrl(){
		return GET_IMAGE_URL;
	}

	private static String getImageUrl(String id) throws IOException, JSONException {
		if(id == null){
			OFFStats.NULL_IDS++;
			return "https://upload.wikimedia.org/wikipedia/commons/f/f8/Question_mark_alternate.svg";
		}
		URL url = new URL("http://world.openfoodfacts.org/api/v0/product/"+ id +".json");
		String input;
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
			input = in.readLine();
			in.close();
		}catch(IOException e){
			e.printStackTrace();
			return "https://upload.wikimedia.org/wikipedia/commons/f/f8/Question_mark_alternate.svg";
		}

		JSONObject json = new JSONObject(input);
		if(json.getString("status_verbose").equals("product not found")){
			System.out.println("Product " + id + " not found");
			OFFStats.PRODUCTS_NOT_FOUND_THROUGH_API++;
			return "https://upload.wikimedia.org/wikipedia/commons/f/f8/Question_mark_alternate.svg";
		}else{
			try {
				return json.getJSONObject("product").getString("image_url");
			} catch (JSONException e) {
				OFFStats.IMAGES_NOT_FOUND++;
				System.out.println("No image found for product " + id);
			}
		}
		return "https://upload.wikimedia.org/wikipedia/commons/f/f8/Question_mark_alternate.svg";
	}
}
