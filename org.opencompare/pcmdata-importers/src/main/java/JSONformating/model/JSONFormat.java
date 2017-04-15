package JSONformating.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.mongodb.util.JSON;

//import kotlin.deprecated;

public class JSONFormat {
	private String quote = "\"";
	private String qcq = quote + ":" + quote;
	private String commaquo = "," + quote;

	private String name;
	private String license;
	private String source;
	private String creator;
	private String primaryFeatureID;
	private List<JFeature> features = new ArrayList<>();
	private List<JProduct> products = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPrimaryFeatureID() {
		return primaryFeatureID;
	}

	public void setPrimaryFeatureID(String primaryFeatureID) {
		this.primaryFeatureID = primaryFeatureID;
	}

	public void addFeature(JFeature f){
		features.add(f);
	}

	public void addProduct(JProduct p){
		products.add(p);
	}

	public boolean isPrimaryFeature(JFeature f) {
		return f.getId().equals(primaryFeatureID);
	}


	public String exportGson(){
		Gson gson = new Gson();

		return gson.toJson(this);
	}

	public String exportHeader(){
		String header = quote + "name" + qcq + name + quote;
		header += commaquo + "license" + qcq + license + quote;
		header += commaquo + "source" + qcq + source + quote;
		header += commaquo + "creator" + qcq + creator + quote;
		header += commaquo + "primaryFeatureID" + qcq + primaryFeatureID + quote;
		return header;
	}

	public JSONObject exportFeature(JFeature f){
		JSONObject feature = new JSONObject();
		feature.put("id", f.getId());
		feature.put("name", f.getName());
		feature.put("type", f.getType().toString());
		return feature;
	}

	@Deprecated
	public String exportFeatureOLD(JFeature f){
		String feature = quote + f.getId() + quote + ":{"; //open feature f
		feature += quote + "id" + qcq + f.getId() + quote;
		feature += commaquo + "name" + qcq + f.getName() + quote;
		feature += commaquo + "type" + qcq + f.getType().toString() + quote;
		feature += "}"; //close feature f
		return feature;
	}

	public String exportFeatures(){
		JSONObject features = new JSONObject();
		for(JFeature f : this.features){
			features.put(f.getId(), exportFeature(f));
		}
		//		String features = quote + "features" + quote + ":{"; //open features
		//		for(JFeature f : this.features){
		//			features += exportFeature(f) + (this.features.indexOf(f) == this.features.size()-1 ? "" : ",");
		//		}
		//		features += "}"; //close features
		return quote + "features" + quote + ":" + features.toString();
	}

	@Deprecated
	public String exportFeaturesOLD(){
		String features = quote + "features" + quote + ":{"; //open features
		for(JFeature f : this.features){
			features += exportFeature(f) + (this.features.indexOf(f) == this.features.size()-1 ? "" : ",");
		}
		features += "}"; //close features
		return features;
	}

	public JSONObject exportCell(JCell c){
		JSONObject cell = new JSONObject();
		cell.put("productID", c.getProductID());
		cell.put("featureID", c.getFeatureID());
		cell.put("type", c.getType());
		//		cell.put("isPartial", false);
		//		cell.put("unit", "undefined");
		cell.put("value", (c.getValue() == null ? "undefined" : c.getValue().export()));
		return cell;
	}

	@Deprecated
	public String exportCellOLD(JCell c){
		String cell = quote + c.getId() + quote + ":{"; //open cell
		cell += quote + "productID" + qcq + c.getProductID() + quote;
		cell += commaquo + "featureID" + qcq + c.getFeatureID() + quote;
		cell += commaquo + "type" + qcq + c.getType() + quote;
		//		cell += commaquo + "isPartial" + qcq + "false" + quote; //FIXME temp isPartial value
		//		cell += commaquo + "unit" + qcq + "undefined" + quote; //FIXME temp unit value
		cell += commaquo + "value" + quote + ":" + (c.getValue() == null ? quote + "undefined" + quote : c.getValue().export());
		cell += "}";//close cell
		return cell;
	}

	public String exportProduct(JProduct p){
		JSONObject prod = new JSONObject();
		JSONObject cells = new JSONObject();
		for(JCell c : p.getCells()){
			cells.put(c.getId(), exportCell(c));
		}
		prod.put("cells", cells);
		return quote + p.getId() + "\":" + prod.toString();
	}

	@Deprecated
	public String exportProductOLD(JProduct p){
		String sProduct = quote + p.getId() + quote + ":{"; //open product
		sProduct += quote + "cells" + quote + ":{"; //open cells
		for(JCell c : p.getCells()){
			sProduct += exportCell(c) + (p.getCells().indexOf(c) == p.getCells().size()-1 ? "" : ",");
		}
		sProduct += "}"//close cells
				+ "}"; //close product
		return sProduct;
	}

	public String exportProducts(PrintWriter out){
		String sProducts = quote + "products"+ quote + ":{"; //open products
		for(JProduct p : products){
			sProducts += exportProduct(p) + (products.indexOf(p) == products.size()-1 ? "" : ",");
			if(out != null){
				out.print(sProducts);
				sProducts = "";
			}
		}
		sProducts += "}"; //close products
		if(out != null){
			out.print(sProducts);
			sProducts = null;
		}
		return sProducts;
	}

	public String export(){
		System.out.println("Exporting...");

		String res = "{"; //open pcm

		res += exportHeader();
		res += ",";
		res += exportFeatures();
		res += ",";
		res += exportProducts(null);
		res += "}"; //close pcm
		return res;
	}

	public void exportToFile(String filename){
		File file = new File(filename);
		try{
			//file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);

			String res = "{" + exportHeader() + ",";
			out.print(res);
			res = exportFeatures() + ",";
			out.print(res);
			exportProducts(out);
			res = "}";
			out.print(res);
			bw.close();

		} catch (IOException e) {
			System.err.println("ERROR exporting to " + filename);
		}

	}

	public String toString(){
		String res = "Name:" + name + " primaryFeatureID: " + primaryFeatureID + "\n\nFeatures:\n";
		for(JFeature f : features){
			res += 	f.getId() + " " +
					f.getName() + " " + 
					f.getType().toString() + "\n";
		}
		res += "\nProducts:\n";
		for(JProduct p : products){
			for(JCell c : p.getCells()){
				res += 	c.getId() + " " + 
						c.getProductID() + " " +
						c.getFeatureID() + " " +
						(c.getType()==null? 
								"" : 
									c.getType().toString()) + " " +
									(c.getValue()==null? 
											"" : 
												(c.getValue().getClass().equals(JMultipleValue.class)?
														((JMultipleValue)c.getValue()).toString():
															c.getValue().getValue().toString())) + "\n";
			}
		}

		return res;
	}

	public List<JFeature> getFeatures() {
		return features;
	}

	public List<JProduct> getProducts() {
		return products;
	}


	public JSONFormatType getTypeForFeature(String featureID){
		Set<JSONFormatType> types = new HashSet<>();
		for(JProduct p : products){
			for(JCell c : p.getCells()){
				if(c.getFeatureID().equals(featureID)){
					types.add(c.getType());
				}
			}
		}
		if(types.size() == 1){
			return types.toArray(new JSONFormatType[0])[0];
		}
		if(types.size() == 2 && types.contains(JSONFormatType.UNDEFINED)){ //if only undefined and 1 other type the return this other type
			types.remove(JSONFormatType.UNDEFINED);
			return types.toArray(new JSONFormatType[0])[0];
		}
		List<JSONFormatType> input = new ArrayList<>();
		input.add(JSONFormatType.BOOLEAN);
		input.add(JSONFormatType.INTEGER);
		input.add(JSONFormatType.REAL);
		input.add(JSONFormatType.UNDEFINED);
		if(containsOnly(types, input)){
			return JSONFormatType.REAL; //bool, int, real and undef are grouped as reals
		}
		input.add(JSONFormatType.STRING);
		input.add(JSONFormatType.DATE);
		input.add(JSONFormatType.IMAGE);
		input.add(JSONFormatType.URL);
		input.add(JSONFormatType.VERSION);
		if(containsOnly(types, input)){
			return JSONFormatType.STRING; //all of the above plus string, date, image, url and version are grouped as strings
		}
		return JSONFormatType.UNDEFINED; //undefined if multiples are mixed up
	}

	private boolean containsOnly(Set<JSONFormatType> set, List<JSONFormatType> input){
		for(JSONFormatType t : set){
			if(!input.contains(t)){ //si t n'est pas dans input, set ne contient pas uniquement des elements de input
				return false;
			}
		}
		return true;
	}


	public boolean sameFeatures(JSONFormat jf, Map<String, String> featLinks){
		List<JFeature> tempFeat = new ArrayList<>(this.features);
		if(jf.getFeatures().size() != this.features.size()){
			return false;
		}
		for(JFeature jfF : jf.getFeatures()){
			for(JFeature thisF : this.features){
				if(tempFeat.contains(thisF)){
					if(this.isPrimaryFeature(thisF) == jf.isPrimaryFeature(jfF) && jfF.sameFeature(thisF)){
						featLinks.put(thisF.getId(), jfF.getId());
						tempFeat.remove(thisF);
					}
				}
			}
		}

		System.out.println(featLinks.toString());
		System.out.println(tempFeat.size() + " features are different : ");
		for(JFeature p : tempFeat){
			System.out.print(p.getId() + ", ");
		}
		return tempFeat.isEmpty();
	}

	public boolean sameProducts(JSONFormat jf, Map<String, String> featLinks, boolean exactContent) {
		List<JProduct> tempProd = new ArrayList<>(this.products);
		if(jf.getProducts().size() != this.products.size()){
			System.out.println("Different number of products");
			return false;
		}
		for(JProduct jfP : jf.getProducts()){
			for(JProduct thisP : this.products){
				if(tempProd.contains(thisP)){
					if(thisP.sameProduct(jfP, featLinks, exactContent)){
						tempProd.remove(thisP);
					}	
				}
			}
		}
		System.out.println("The following " + tempProd.size() + "/" + this.products.size() + " products from 'this' are not found in JSONFormat in parameter : ");
		for(JProduct p : tempProd){
			System.out.print(p.getId() + ", ");
		}
		return tempProd.isEmpty();
	}

	/**
	 * Checks if the JSONFormat in parameter is the same as this, omitting ids
	 * @param jf the format to compare
	 * @return true if this and jf are the same, omits ids
	 */
	public boolean equals(JSONFormat jf){ 
		Map<String,String> featLinks = new HashMap<>();
		if(!this.name.equals(jf.name) || !this.creator.equals(jf.creator) || !this.license.equals(jf.license) || !this.source.equals(jf.source)){
			return false;
		}else if(!this.sameFeatures(jf, featLinks)){
			System.out.println("\nDifferences in features");
			return false;
		}else if(!this.sameProducts(jf, featLinks, true)){
			System.out.println("\nDifferences in products");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the JSONFormat in parameter is the same as this, omitting ids and jcell.jvalue.value content
	 * @param jf the format to compare
	 * @return true if this and jf are the same, omits ids
	 */
	public boolean sameJSONFormat(JSONFormat jf){ 
		Map<String,String> featLinks = new HashMap<>();
		if(!this.name.equals(jf.name) || !this.creator.equals(jf.creator) || !this.license.equals(jf.license) || !this.source.equals(jf.source)){
			return false;
		}else if(!this.sameFeatures(jf, featLinks)){
			System.out.println("\nDifferences in features");
			return false;
		}else if(!this.sameProducts(jf, featLinks, false)){
			System.out.println("\nDifferences in products");
			return false;
		}
		return true;
	}
	
}
