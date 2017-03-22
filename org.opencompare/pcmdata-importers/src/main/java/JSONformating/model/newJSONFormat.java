package JSONformating.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class newJSONFormat {

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

	public String export(){
		System.out.println("Exporting...");

		String res = "pcm:{"; //open pcm

		res += "name:\"" + name;
		res += "\",license:\"" + license;
		res += "\",source:\"" + source;
		res += "\",creator:\"" + creator;
		res += "\",primaryFeatureID:\"" + primaryFeatureID;
		res += "\",features:{"; //open features

		for(JFeature f : features){
			res += f.getId() + ":{"; //open feature f
			res += "id:\"" + f.getId();
			res += "\",name:\"" + f.getName();
			res += "\",type:\"" + f.getType().toString();
			res += "\"},"; //close feature f
		}

		res = features.isEmpty() ? res : res.substring(0, res.length() - 1);
		System.out.println("Features exported");
		res += "},"; //close features
		res += "products:{"; //open products

		for(JProduct p : products){
			res += p.getId() + ":{"; //open product
			res += "cells:{"; //open cells
			for(JCell c : p.getCells()){
				res += c.getId() + ":{"; //open cell
				res += "productID:\"" + c.getProductID();
				res += "\",featureID:\"" + c.getFeatureID();
				res += "\",type:\"" + c.getType();
				res += "\",isPartial:\"" + "false"; //FIXME temp isPartial value
				res += "\",unit:\"" + "undefined"; //FIXME temp isPartial value
				res += "\",value:" + (c.getValue() == null ? "undefined" : c.getValue().export());
				res += "},";//close cell
			}
			res = p.getCells().isEmpty() ? res : res.substring(0, res.length() - 1);
			res += "}";
			res += "},"; //close product
		}
		res = products.isEmpty() ? res : res.substring(0, res.length() - 1);
		System.out.println("Products exported");
		res += "}"; //close products
		res += "}"; //close pcm
		return res;
	}

	public void exportToFile(String filename){
		File file = new File(filename);
		try{
			FileWriter fw;
			BufferedWriter bw;
			if(file.exists()){
				fw = new FileWriter(file, false);
				bw = new BufferedWriter(fw);
				bw.write("");
			}
			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);

			String res = "pcm:{"; //open pcm

			res += "name:\"" + name;
			res += "\",license:\"" + license;
			res += "\",source:\"" + source;
			res += "\",creator:\"" + creator;
			res += "\",primaryFeatureID:\"" + primaryFeatureID;
			res += "\",features:{"; //open features

			for(JFeature f : features){
				res += f.getId() + ":{"; //open feature f
				res += "id:\"" + f.getId();
				res += "\",name:\"" + f.getName();
				res += "\",type:\"" + f.getType().toString();
				res += "\"},"; //close feature f
			}

			res = features.isEmpty() ? res : res.substring(0, res.length() - 1);
			res += "},"; //close features

			out.print(res); //print features
			res = ""; //reset string

			res += "products:{"; //open products

			for(JProduct p : products){
				res += p.getId() + ":{"; //open product
				res += "cells:{"; //open cells
				for(JCell c : p.getCells()){
					res += c.getId() + ":{"; //open cell
					res += "productID:\"" + c.getProductID();
					res += "\",featureID:\"" + c.getFeatureID();
					res += "\",type:\"" + c.getType();
					res += "\",isPartial:\"" + "false"; //FIXME temp isPartial value
					res += "\",unit:\"" + "undefined"; //FIXME temp isPartial value
					res += "\",value:" + (c.getValue() == null ? "undefined" : c.getValue().export());
					res += "},";//close cell
				}
				res = p.getCells().isEmpty() ? res : res.substring(0, res.length() - 1);

				res += "}";
				out.print(res);
				res = "";
				res += "},"; //close product

			}
			res = products.isEmpty() ? res : res.substring(0, res.length() - 1);
			res += "}"; //close products
			res += "}"; //close pcm
			out.print(res);
			out.close();
			System.out.println("Exported");
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

	public newJSONFormatType getTypeForFeature(String featureID){
		Set<newJSONFormatType> types = new HashSet<>();
		for(JProduct p : products){
			for(JCell c : p.getCells()){
				if(c.getFeatureID().equals(featureID)){
					types.add(c.getType());
				}
			}
		}
		if(types.size() == 1){
			newJSONFormatType[] a = new newJSONFormatType[0];;
			return types.toArray(a)[0];
		}
		List<newJSONFormatType> input = new ArrayList<>();
		input.add(newJSONFormatType.BOOLEAN);
		input.add(newJSONFormatType.INTEGER);
		input.add(newJSONFormatType.REAL);
		input.add(newJSONFormatType.UNDEFINED);
		if(containsOnly(types, input)){
			return newJSONFormatType.REAL; //bool, int, real and undef are grouped as reals
		}
		input.add(newJSONFormatType.STRING);
		input.add(newJSONFormatType.DATE);
		input.add(newJSONFormatType.IMAGE);
		input.add(newJSONFormatType.URL);
		input.add(newJSONFormatType.VERSION);
		if(containsOnly(types, input)){
			return newJSONFormatType.STRING; //all of the above plus string, date, image, url and version are grouped as strings
		}
		return newJSONFormatType.UNDEFINED; //undefined if multiples are mixed up
	}

	private boolean containsOnly(Set<newJSONFormatType> set, List<newJSONFormatType> input){
		for(newJSONFormatType t : set){
			if(!input.contains(t)){ //si t n'est pas dans input, set ne contient pas uniquement des elements de input

				return false;
			}
		}
		return true;
	}

}
