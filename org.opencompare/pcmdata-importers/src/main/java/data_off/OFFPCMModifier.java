package data_off;

import java.io.IOException;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.value.MultipleImpl;
import org.opencompare.api.java.value.Multiple;
import org.opencompare.api.java.value.StringValue;


public class OFFPCMModifier {

	public static void addMultiplesToFile(String category) throws IOException{
		
		category = category.replace(":", "_");
		String filename = "off_output/pcms/"+category+".pcm";
		PCM pcm = PCMUtil.loadPCM(filename);

		//check(pcm);
		
		pcm = computeMultiples(pcm);
		
		//System.in.read();
		
		//check(pcm);
		

		PCMInterpreter._serializeToPCMJSON(new PCMContainer(pcm), "off_output/pcms/"+category+"_m.pcm");
		System.out.println("DONE");
	}
	
	private static void check(PCM pcm) {
		for(Feature f : pcm.getConcreteFeatures()){
			if(isMultiple(f.getName())){
				for(Cell c : f.getCells()){
					for (Value val : ((Multiple)c.getInterpretation()).getSubValues()) {
						System.out.println(((StringValue) val).getValue());
					}
				}
			}else if(f.getName().equals("image_url")){
				for(Cell c : f.getCells()){
					System.out.println(c.getRawContent());
				}
				//System.out.println(f.getCells().get(0).getInterpretation().getClass().getName());
			}
		}
	}

	public static PCM computeMultiples(PCM pcm){
		for(Feature f : pcm.getConcreteFeatures()){
			if(isMultiple(f.getName())){
				for(Cell c : f.getCells()){
					Multiple multiple = toMultipleValue(c.getContent());
					c.setInterpretation(multiple);
				}
			}else if(f.getName().equals("image_url")){
				for(Cell c : f.getCells()){
					PCMFactory fac = new PCMFactoryImpl();
					StringValue str = fac.createStringValue();
					str.setValue(c.getRawContent());
					c.setInterpretation(str);
				}
				System.out.println("image_url feature reverted back from MultipleValue to StringValue");
			}else if(f.getName().equals("product_name")){
				/*
				 * l'outil de generation de pcm a partir de csv prend des libertes assez etranges comme convertir "Mike and Ike" en ["Mike", "Ike"] (Multiple)
				 * et aussi avec "/" et "," mais pas tout le temps
				 */
				for(Cell c : f.getCells()){
					if(c.getInterpretation().getClass().equals(MultipleImpl.class)){
						PCMFactory fac = new PCMFactoryImpl();
						StringValue str = fac.createStringValue();
						str.setValue(c.getRawContent());
						c.setInterpretation(str);
					}
				}
				System.out.println("converted eventual MultipleValues to StringValues in product_name");
			}
		}
		System.out.println("Multiples computed");
		return pcm;
	}

	private static boolean isMultiple(String name) {
		switch(name){
		case "ingredients":
		case "countries":
		case "stores":
		case "brands":
			return true;

		default : 
			return false;
		}
	}

	private static Multiple toMultipleValue(String content) {
		PCMFactory f = new PCMFactoryImpl();
		Multiple multiple = f.createMultiple();
		for(String s : content.split(OFFProduct.separator)){
			if(!s.isEmpty()){
				StringValue val = f.createStringValue();
				val.setValue(s);
				multiple.addSubValue(val);
			}
		}
		return multiple;
	}
}
