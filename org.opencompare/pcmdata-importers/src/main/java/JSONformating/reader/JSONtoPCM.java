package JSONformating.reader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMMetadata;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.FeatureImpl;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.util.ComplexePCMElementComparator;
import org.opencompare.api.java.util.DiffResult;
import org.opencompare.api.java.util.PCMElementComparator;
import org.opencompare.api.java.value.BooleanValue;
import org.opencompare.api.java.value.DateValue;
import org.opencompare.api.java.value.IntegerValue;
import org.opencompare.api.java.value.Multiple;
import org.opencompare.api.java.value.RealValue;
import org.opencompare.api.java.value.StringValue;
import org.opencompare.api.java.value.Version;

import JSONformating.PCMtoJSON;
import JSONformating.model.JBooleanValue;
import JSONformating.model.JCell;
import JSONformating.model.JFeature;
import JSONformating.model.JMultipleValue;
import JSONformating.model.JNumberValue;
import JSONformating.model.JProduct;
import JSONformating.model.JSONFormat;
import JSONformating.model.JStringValue;
import JSONformating.model.JValue;

public class JSONtoPCM {
	
	public static Map<String, Feature> featuresMap = new HashMap<>();
	
	public static PCMContainer JSONFormatToPCM(JSONFormat jf){
		PCMFactoryImpl factory = new PCMFactoryImpl();
		PCM pcm = factory.createPCM();
		
		PCMMetadata meta = new PCMMetadata(pcm);
		PCMContainer pcmC = new PCMContainer(pcm);
		
		pcmC.setMetadata(meta);
		meta.setCreator(jf.getCreator());
		meta.setLicense(jf.getLicense());
		meta.setSource(jf.getSource());
		
		pcm.setName(jf.getName());
		
		importFeatures(jf, pcm, factory);
		Logger.getGlobal().info("Features imported from JSONFormat to PCM");
		importProducts(jf, pcm, factory);
		
		return pcmC;
	}

	private static void importFeatures(JSONFormat jf, PCM pcm, PCMFactoryImpl factory) {
		Feature feature;
		for(JFeature f : jf.getFeatures()){
			feature = factory.createFeature();
			feature.setName(f.getName());
			pcm.addFeature(feature);
			if(jf.isPrimaryFeature(f)){
				pcm.setProductsKey(feature);
			}
			featuresMap.put(f.getId(), feature);
		}
		
	}
	
	private static void importProducts(JSONFormat jf, PCM pcm, PCMFactoryImpl factory) {
		Product product;
		Cell cell;
		int temp = 0;
		for(JProduct p : jf.getProducts()){
			product = factory.createProduct();
			for(JCell c : p.getCells()){
				cell = factory.createCell();
				cell.setFeature(featuresMap.get(c.getFeatureID()));
				cell.setInterpretation(getInterpretation(c, factory));
				product.addCell(cell);
			}
			pcm.addProduct(product);
		}
		
	}

	private static Value getInterpretation(JCell cell, PCMFactoryImpl factory) {
		Value value = null;
		switch(cell.getType()){
		case BOOLEAN:
			value = factory.createBooleanValue();
			((BooleanValue) value).setValue(((JBooleanValue) cell.getValue()).getValue());
			break;
		case DATE:
			value = factory.createDateValue();
			((DateValue) value).setValue(((JStringValue) cell.getValue()).getValue());
			break;
		case IMAGE:case STRING:case URL:
			value = factory.createStringValue();
			((StringValue) value).setValue(((JStringValue) cell.getValue()).getValue());
			break;
		case VERSION:
			value = factory.createVersion();
			//((Version) value).setValue(?????) //TODO
			break;
		case INTEGER:
			value = factory.createIntegerValue();
			((IntegerValue) value).setValue(((JNumberValue) cell.getValue()).getAsInteger());
			break;
		case REAL:
			value = factory.createRealValue();
			((RealValue) value).setValue(((JNumberValue) cell.getValue()).getValue());
			break;
		case MULTIPLE:
			value = factory.createMultiple();
			setSubvaluesForMultiple((Multiple) value, (JMultipleValue) cell.getValue(), factory);
			break;
		case UNDEFINED:
			break;
		default:
			break;
		
		}
		return value;
	}

	private static void setSubvaluesForMultiple(Multiple value, JMultipleValue mulValue, PCMFactoryImpl factory) {
		Value newVal;
		for(JValue v : mulValue.getValue()){
			if(v instanceof JBooleanValue){
				newVal = factory.createBooleanValue();
				((BooleanValue) newVal).setValue(((JBooleanValue) v).getValue());
			}else if(v instanceof JStringValue){
				newVal = factory.createStringValue();
				((StringValue) newVal).setValue(((JStringValue) v).getValue());
			}else if( v instanceof JNumberValue){
				Double d = ((JNumberValue) v).getValue();
				if(d == null){
					System.out.println();
				}
				if(d.intValue() == d){
					newVal = factory.createIntegerValue();
					((IntegerValue) newVal).setValue(d.intValue());
				}else{
					newVal = factory.createRealValue();
					((RealValue) newVal).setValue(d);
				}
			}else{
				newVal = factory.createNotAvailable();				
			}
			value.addSubValue(newVal);
		}
	}

	public static void main(String[] args) throws IOException {
		String filename = "output-pcm/tests/muted_Comparison_of_VoIP_software_0.new.pcm";
		String filename2 = "off_output/pcms/fr_biscottes-pauvres-en-sel.new.pcm";
		
		JSONFormat jf = JSONReader.importJSON(filename);
		
		PCMContainer pcmC = JSONFormatToPCM(jf);
		
		JSONFormat jf2 = PCMtoJSON.mkNewJSONFormatFromPCM(pcmC);

//		JSONFormat jf3 = JSONReader.importJSON(filename2);
		
		PCMContainer pcmC2 = JSONFormatToPCM(jf2);
		
//		PCMContainer pcmC3 = JSONFormatToPCM(jf3);
		
		DiffResult res = pcmC.getPcm().diff(pcmC2.getPcm(), new ComplexePCMElementComparator());

		System.out.println(res.toString());
		
		DiffResult res2 = pcmC2.getPcm().diff(pcmC.getPcm(), new ComplexePCMElementComparator());
		
		System.out.println(res2.toString());
		
//		System.out.println("1."+jf.exportProducts(null));
//		System.out.println("2."+jf2.exportProducts(null));
//		System.out.println(pcmC.getPcm().isValid() + "\n");
//		System.out.println(pcmC.getPcm().getProductsKey().getName());
//		jf.exportToFile("off_output/pcms/test.pcm");
//		jf2.exportToFile("off_output/pcms/test2.pcm");
		System.out.println("\n" + jf.sameJSONFormat(jf2));
	}

}
