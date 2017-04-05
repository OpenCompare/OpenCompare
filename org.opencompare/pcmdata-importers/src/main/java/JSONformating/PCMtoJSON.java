package JSONformating;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.value.*;
import org.opencompare.api.java.value.*;

import JSONformating.model.*;
import data_off.PCMInterpreter;
import data_off.PCMUtil;

public class PCMtoJSON {

	public static JSONFormat mkNewJSONFormatFromPCM(PCMContainer pcmC) throws IOException{

		PCM pcm = pcmC.getPcm();
		JSONFormat nJSONf = new JSONFormat();

		nJSONf.setName(pcm.getName());
		nJSONf.setCreator(pcmC.getMetadata().getCreator());
		nJSONf.setLicense(pcmC.getMetadata().getLicense());
		nJSONf.setSource(pcmC.getMetadata().getSource());

		Map<Feature,String> features = new HashMap<>();

		int fCount = 0;
		JFeature jf;
		for(Feature f : pcm.getConcreteFeatures()){
			jf = new JFeature();
			jf.setId("F" + fCount);
			jf.setName(f.getName());
			jf.setType(JSONFormatType.UNDEFINED);//TODO
			nJSONf.addFeature(jf);
			features.put(f, jf.getId());
			fCount++;
		}

		int pCount = 0;
		int cCount = 0;
		JProduct jp;
		JCell jc;
		for(Product p : pcm.getProducts()){
			jp = new JProduct();
			nJSONf.addProduct(jp);
			jp.setId("P" + pCount);
			for(Cell c : p.getCells()){
				jc = new JCell();

				jc.setId("C" + cCount);
				jc.setFeatureID(features.get(c.getFeature()));
				jc.setProductID(jp.getId());

				JValue value = createJValueFromCellForJCell(c, jc);

				jc.setValue(value);

				jp.getCells().add(jc);
				cCount++;
			}
			pCount++;
		}

		String primaryFeatureID = features.get(pcm.getProductsKey());
		nJSONf.setPrimaryFeatureID(primaryFeatureID);

		addTypesToFeatures(nJSONf);

		return nJSONf;

	}

	private static void addTypesToFeatures(JSONFormat nJSONf) {
		for(JFeature jf : nJSONf.getFeatures()){
			jf.setType(nJSONf.getTypeForFeature(jf.getId()));
		}

	}

	public static JValue createJValueFromCellForJCell(Cell c, JCell jc) throws IOException{
		Value value =  c.getInterpretation();
		if(value instanceof BooleanValueImpl){

			jc.setType(JSONFormatType.BOOLEAN);
			JBooleanValue bool = new JBooleanValue();
			bool.setValue(((BooleanValue) value).getValue());
			return bool;

		}else if(value instanceof DateValueImpl){

			jc.setType(JSONFormatType.DATE);
			JStringValue stringValue = new JStringValue();
			stringValue.setValue(((DateValue) value).getValue());
			return stringValue;

		}else if(value instanceof IntegerValueImpl){

			jc.setType(JSONFormatType.INTEGER);
			JNumberValue numValue = new JNumberValue();
			numValue.setValue(((IntegerValue) value).getValue());
			return numValue;

		}else if(value instanceof RealValueImpl){

			jc.setType(JSONFormatType.REAL);
			JNumberValue numValue = new JNumberValue();
			numValue.setValue(((RealValue) value).getValue());
			return numValue;

		}else if(value instanceof StringValueImpl){

			StringValue sv = (StringValue) value;
			if(Pattern.matches("^\\s*http:\\/\\/.*"
					+ "|^\\s*https:\\/\\/.*", sv.getValue())
			&& Pattern.matches(".*\\.jpg\\s*$"
					+ "|.*\\.svg\\s*$"
					+ "|.*\\.jpeg\\s*$"
					+ "|.*\\.bmp\\s*$"
					+ "|.*\\.png\\s*$"
					+ "|.*\\.gif\\s*$", sv.getValue())){
				jc.setType(JSONFormatType.IMAGE);
			}else if(Pattern.matches("^\\s*http:\\/\\/.*"
					+ "|^\\s*https:\\/\\/.*", sv.getValue())){
				jc.setType(JSONFormatType.URL);
			}else{
				jc.setType(JSONFormatType.STRING);
			}
			JStringValue stringValue = new JStringValue();
			stringValue.setValue(((StringValue) value).getValue());
			return stringValue;
		}else if(value instanceof MultipleImpl){
			jc.setType(JSONFormatType.MULTIPLE);
			JMultipleValue mulvalue = new JMultipleValue();
			mulvalue.setValue(createJValuesForMultiple(((Multiple) value).getSubValues()));
			return mulvalue;
		}else if(value instanceof VersionImpl){
			jc.setType(JSONFormatType.VERSION);
			JStringValue stringValue = new JStringValue();
			stringValue.setValue(""); //FIXME Version ? .api ? .model ?
			return stringValue;
			
		}else if(value instanceof NotApplicableImpl){
			jc.setType(JSONFormatType.UNDEFINED);
		}else{
			jc.setType(JSONFormatType.UNDEFINED);
		}
		return null;
	}

	public static List<JValue> createJValuesForMultiple(List<Value> values){
		List<JValue> jvalues = new ArrayList<>();
		
		for(Value val : values){
			if(val instanceof BooleanValueImpl){
				JBooleanValue value = new JBooleanValue();
				value.setValue(((BooleanValue) val).getValue());
				jvalues.add(value);
			}else if(val instanceof DateValueImpl){
				JStringValue value = new JStringValue();
				value.setValue(((DateValue) val).getValue());
				jvalues.add(value);
			}else if(val instanceof IntegerValueImpl){
				JNumberValue value = new JNumberValue();
				value.setValue(((IntegerValue) val).getValue());
				jvalues.add(value);
			}else if(val instanceof RealValueImpl){
				JNumberValue value = new JNumberValue();
				value.setValue(((RealValue) val).getValue());
				jvalues.add(value);
			}else if(val instanceof StringValueImpl){
				JStringValue value = new JStringValue();
				value.setValue(((StringValue) val).getValue());
				jvalues.add(value);
			}else if(val instanceof MultipleImpl){
				
			}else if(val instanceof VersionImpl){
				
			}else if(val instanceof NotApplicableImpl){
				
			}else{
				
			}
			

		}
		return jvalues;
	}

	public static void main(String[] args) throws IOException  {

		String inFilename = "off_output/pcms/en_french-blue-veined-cheeses.pcm";
		String outFilename = "off_output/pcms/en_french-blue-veined-cheeses.new.pcm";
		PCMContainer pcmC = PCMUtil.loadPCMContainer(inFilename);
		System.out.println("PCM loaded");
		JSONFormat nf = mkNewJSONFormatFromPCM(pcmC);
		System.out.println("new format created");
//		String jsonRes = nf.export();
//		System.out.println(jsonRes);
		nf.exportToFile(outFilename);
//		PCMInterpreter.writeToFile(outFilename, jsonRes);

//		JsonElement jelement = new JsonParser().parse(jsonRes);
		System.out.println("EXPORT OK");
		
		Scanner scanner = new Scanner(new File(outFilename));
		String json = scanner.useDelimiter("\\Z").next();
		scanner.close();
//		System.out.println(json);
		JsonElement jelement = new JsonParser().parse(json);
		System.out.println("IMPORT OK");
//		System.out.println(jelement.toString());
	}

}

