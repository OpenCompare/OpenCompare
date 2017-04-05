package JSONformating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.value.*;
import org.opencompare.api.java.value.*;

import JSONformating.model.*;
import data_off.PCMInterpreter;
import data_off.PCMUtil;

public class PCMtonewJSON {

	public static newJSONFormat mkNewJSONFormatFromPCM(PCMContainer pcmC){

		PCM pcm = pcmC.getPcm();
		newJSONFormat nJSONf = new newJSONFormat();

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
			jf.setType(newJSONFormatType.UNDEFINED);//TODO
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

	private static void addTypesToFeatures(newJSONFormat nJSONf) {
		for(JFeature jf : nJSONf.getFeatures()){
			jf.setType(nJSONf.getTypeForFeature(jf.getId()));
		}

	}

	public static JValue createJValueFromCellForJCell(Cell c, JCell jc){
		Value value =  c.getInterpretation();
		if(value instanceof BooleanValueImpl){

			jc.setType(newJSONFormatType.BOOLEAN);
			JBooleanValue bool = new JBooleanValue();
			bool.setValue(((BooleanValue) c.getInterpretation()).getValue());
			return bool;

		}else if(value instanceof DateValueImpl){

			jc.setType(newJSONFormatType.DATE);
			JStringValue stringValue = new JStringValue();
			stringValue.setValue(((DateValue) c.getInterpretation()).getValue());
			return stringValue;

		}else if(value instanceof IntegerValueImpl){

			jc.setType(newJSONFormatType.INTEGER);
			JNumberValue numValue = new JNumberValue();
			numValue.setValue(((IntegerValue) c.getInterpretation()).getValue());
			return numValue;

		}else if(value instanceof RealValueImpl){

			jc.setType(newJSONFormatType.REAL);
			JNumberValue numValue = new JNumberValue();
			numValue.setValue(((RealValue) c.getInterpretation()).getValue());
			return numValue;

		}else if(value instanceof StringValueImpl){

			StringValue sv = (StringValue) c.getInterpretation();
			if(Pattern.matches("^\\s*http:\\/\\/.*"
					+ "|^\\s*https:\\/\\/.*", sv.getValue())
			&& Pattern.matches(".*\\.jpg\\s*$"
					+ "|.*\\.svg\\s*$"
					+ "|.*\\.jpeg\\s*$"
					+ "|.*\\.bmp\\s*$"
					+ "|.*\\.png\\s*$"
					+ "|.*\\.gif\\s*$", sv.getValue())){
				jc.setType(newJSONFormatType.IMAGE);
			}else if(Pattern.matches("^\\s*http:\\/\\/.*"
					+ "|^\\s*https:\\/\\/.*", sv.getValue())){
				jc.setType(newJSONFormatType.URL);
			}else{
				jc.setType(newJSONFormatType.STRING);
			}
			JStringValue stringValue = new JStringValue();
			stringValue.setValue(((StringValue) c.getInterpretation()).getValue());
			return stringValue;
		}else if(value instanceof MultipleImpl){
			jc.setType(newJSONFormatType.MULTIPLE);
			JMultipleValue mulvalue = new JMultipleValue();
			mulvalue.setValue(createJValuesForMultiple(((Multiple) c.getInterpretation()).getSubValues()));
			return mulvalue;
		}else if(value instanceof VersionImpl){
			jc.setType(newJSONFormatType.VERSION);
		}else if(value instanceof NotApplicableImpl){
			jc.setType(newJSONFormatType.UNDEFINED);
		}else{
			jc.setType(newJSONFormatType.UNDEFINED);
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

	public static void main(String[] args) throws IOException {

		String inFilename = "off_output/pcms/fr_biscottes-pauvres-en-sel.pcm";
		String outFilename = "off_output/pcms/fr_biscottes-pauvres-en-sel.test";
		PCMContainer pcmC = PCMUtil.loadPCMContainer(inFilename);
		System.out.println("PCM loaded");
		newJSONFormat nf = mkNewJSONFormatFromPCM(pcmC);
		System.out.println("new format created");
		String jsonRes = nf.export();
		System.out.println(jsonRes);
		PCMInterpreter.writeToFile(outFilename, jsonRes);

		JsonElement jelement = new JsonParser().parse(jsonRes);
		System.out.println(jelement.toString());
	}

}

