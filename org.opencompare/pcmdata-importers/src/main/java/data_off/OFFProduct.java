package data_off;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.bson.Document;

/**
 * OFFProduct class
 * 
 * @author mael
 *
 */
public class OFFProduct {

	public static String separator = ",";

	private final Logger _log = Logger.getLogger(OFFProduct.class.getName());

	private String id;
	private String product_name;
	private List<String> countries;
	private List<String> ingredients;
	private List<String> brands;
	private List<String> stores;
	private Map<String, String> nutriments;
	private String image_url;

	public OFFProduct(){
		this.countries = new ArrayList<>();
		this.ingredients = new ArrayList<>();
		this.brands = new ArrayList<>();
		this.stores = new ArrayList<>();
		this.nutriments = new HashMap<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public List<String> getCountries() {
		return countries;
	}

	public String getCountriesString() throws IOException {
		return listToString(countries);
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public void setCountriesFromString(String countries) {
		try{
			if(!countries.isEmpty()){
				countries = enhanceText(countries);
				this.countries = Arrays.asList(countries.split(separator));
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public String getIngredientsString() throws IOException{
		return listToString(ingredients);
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public List<String> getBrands() {
		return brands;
	}

	public String getBrandsString() throws IOException {
		return listToString(brands);
	}

	public void setBrands(List<String> brands) {
		this.brands = brands;
	}

	public void setBrandsFromString(String brands) {
		try{
			if(!brands.isEmpty()){
				this.brands = Arrays.asList(brands.split(separator));
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
	}

	public List<String> getStores() {
		return stores;
	}

	public String getStoresString() throws IOException {
		return listToString(stores);
	}

	public void setStores(List<String> stores) {
		this.stores = stores;
	}

	public void setStoresFromString(String stores) {
		try{
			if(!stores.isEmpty()){
				this.stores = Arrays.asList(stores.split(separator));
			}
		}
		catch(NullPointerException e){
			_log.warning("Product " + id + " null pointer exception on stores");
		}

	}

	public void setNutrimentsFromObject(Object nutriments){
		try{
			Document nutrimentsDoc = (Document) nutriments;
			Set<Entry<String, Object>> nutrimentsSet =  nutrimentsDoc.entrySet();
			String key, value;
			for(Entry<String, Object> e : nutrimentsSet){
				key = e.getKey();
				value = e.getValue().toString();
				if(!value.isEmpty()){
					if(key.endsWith("_value") && !key.equals("energy_value")){
						this.nutriments.put(key.substring(0, key.length()-6), value);
					}else if(key.equals("energy_100g")){
						if(nutrimentsDoc.getString("energy_unit").toLowerCase().equals("kj")){
							Float val = Float.parseFloat(value);
							val = val * 43 / 180; //kJ to kcal
							value = val.toString();
						}
						this.nutriments.put(key, value);
					}
				}
			}
		}catch(NullPointerException e){
			OFFStats.NULL_NUTRIMENTS++;
			_log.warning("Product " + id + " null pointer exception on nutriments");
		}

	}

	public String getNutrimentValue(String nutriment){
		String val = this.nutriments.get(nutriment); 
		return (val == null)?"0":val;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}


	@SuppressWarnings("unchecked")
	public void setIngredientsFromObject(Object ingredients) {
		try{
			List<Document> ingredientsList = (List<Document>) ingredients;
			String text;
			for(Document o : ingredientsList){
				text = o.getString("text");

				text = enhanceText(text);

				if(!text.isEmpty()){
					this.ingredients.add(text);
				}
			}
		}catch(NullPointerException e){
			_log.warning("Product " + id + " null pointer exception on ingredientsList");
		}
	}

	public static String enhanceText(String text) {
		//replace Single Low-9 Quotation Mark with Comma
		text = text.replace((char)8218, ',');

		//remove _ ( and ) anywhere and remove dot and comma at the end of the string
		text = text.replaceAll("[_)(]|[.,]$", "");

		//remove number at the end of the string if the preceding char is a space //TODO MAYBE DISABLE
		text = text.replaceAll("[ ][0-9]+$", "");

		//remove white spaces in front and back
		text = text.replaceAll("^[\\s]|[\\s]$|[\\n\\r]", "");
		
		//remove all \n
//		text = text.replaceAll("\\n", ""); //FIXME still some ingredients have 

		return text;
	}

	private String listToString(List<String> list){
		String str = "";
		for (String e : list) {
			str += e + separator;
		}
		return (str.isEmpty())?str:str.substring(0, str.length() - separator.length());
	}


}
