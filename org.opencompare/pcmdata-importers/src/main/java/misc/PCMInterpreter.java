package misc;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import data_off.PCMUtil;
import org.junit.Test;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.extractor.CellContentInterpreter;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;


public class PCMInterpreter {

	@Test
	public void testCSV1() throws Exception {
		String csvFileName = "input-csv/Comparison_of_HTML_editors.csv";

		PCMContainer pcmContainer = mkPCMInterpreted(csvFileName);
		_serializeToPCMJSON(pcmContainer, csvFileName + ".json");
		
	}
	
	@Test
	public void testCSV2() throws Exception {
		String csvFileName = "input-csv/FIFA16.csv";

		PCMContainer pcmContainer = mkPCMInterpreted(csvFileName);
		_serializeToPCMJSON(pcmContainer, csvFileName + ".json");
				
	}
	
	

	private void _serializeToPCMJSON(PCMContainer pcmContainer, String jsonFileName) throws IOException {
		KMFJSONExporter exporter = new KMFJSONExporter();
		String json = exporter.export(pcmContainer);
		// Write modified PCM
		writeToFile("" + jsonFileName, json);
	}
	

	public PCMContainer mkPCMInterpreted(String csvFileName) throws IOException {
	
		List<PCMContainer> pcms = PCMUtil.loadCSV(csvFileName); // already interpreted with CellContentInterpreter
		PCMContainer pcmContainer = pcms.get(0);
		
		/* not necessary!
		PCM pcm = pcmContainer.getPcm();


		PCMFactory factory = new PCMFactoryImpl();
		CellContentInterpreter interpreter = new CellContentInterpreter(factory);

		pcm.normalize(factory);
		interpreter.interpretCells(pcm);*/

		return pcmContainer;

	}

	public void writeToFile(String path, String content) throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
		writer.write(content);
		writer.close();
	}

}
