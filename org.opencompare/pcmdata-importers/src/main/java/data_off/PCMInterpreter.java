package data_off;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.extractor.CellContentInterpreter;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.io.CSVLoader;
import org.opencompare.api.java.io.PCMDirection;

import JSONformating.PCMtoJSON;
import JSONformating.model.JSONFormat;


public class PCMInterpreter {

	public static void CSVToPCM(String filename) throws IOException{
		PCMContainer pcmC = mkPCMInterpreted(filename);
		PCM pcm = OFFPCMModifier.computeMultiples(pcmC.getPcm());
		pcmC = new PCMContainer(pcm);
		_serializeToPCMJSON(pcmC,filename.replace("off_output/", "off_output/pcms/").replace(".csv", ".pcm"));
	}
	
	public static void CSVTonewPCM(String filename) throws IOException{
		PCMContainer pcmC = mkPCMInterpreted(filename);
		PCM pcm = OFFPCMModifier.computeMultiples(pcmC.getPcm());
		pcmC = new PCMContainer(pcm);
		JSONFormat nf = PCMtoJSON.mkNewJSONFormatFromPCM(pcmC);
		System.out.println("new format created");
		nf.exportToFile(filename.replace("off_output/", "off_output/pcms/").replace(".csv", ".new.pcm"));
	}

	public static void _serializeToPCMJSON(PCMContainer pcmContainer, String jsonFileName) throws IOException {
		KMFJSONExporter exporter = new KMFJSONExporter();
		String json = exporter.export(pcmContainer);
		// Write modified PCM
		writeToFile(jsonFileName, json);
	}


	public static PCMContainer mkPCMInterpreted(String csvFileName) throws IOException {

		CSVLoader csvL = new CSVLoader(
				new PCMFactoryImpl(),
				new CellContentInterpreter(new PCMFactoryImpl()), 
				';', '"',
				PCMDirection.PRODUCTS_AS_LINES);
		List<PCMContainer> pcmC = csvL.load(new File(csvFileName));
		PCMContainer pcmContainer = pcmC.get(0);
		PCM pcm = pcmContainer.getPcm();
		for(Feature f : pcm.getConcreteFeatures()){
			if(f.getName().equals("product_name")){
				pcm.setProductsKey(f);
				System.out.println("Key changed");
				break;
			}
		}
		
//		System.out.println("--- Products ---");
//		for (AbstractFeature product : pcmContainer.getPcm().getFeatures()) {
//			System.out.println(product.getName());
//		}
		
		return pcmContainer;

	}

	public static void writeToFile(String path, String content) throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
		writer.write(content);
		writer.close();
	}

}
