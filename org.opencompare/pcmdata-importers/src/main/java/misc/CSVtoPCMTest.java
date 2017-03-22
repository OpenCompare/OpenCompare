package misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.io.PCMDirection;

public class CSVtoPCMTest {

	@Ignore
	@Test
	public void csv1() throws Exception {
		
		String csvFileName = "input-csv/grxyvnpyuqlmwwdidyuyuzokvbpp.csv";
		List<PCMContainer> pcms = PCMUtil.loadCSV(csvFileName); // already interpreted with CellContentInterpreter
		PCMContainer pcmContainer = pcms.get(0);
		assertNotNull(pcmContainer);
		
		assertEquals(5000, pcmContainer.getPcm().getProducts().size());
		
		KMFJSONExporter jsonExporter = new KMFJSONExporter();
        
		String pcmString = jsonExporter.export(pcmContainer);

		Path p = Paths.get("output/" + "grxyvnpyuqlmwwdidyuyuzokvbpp.json");
		try {
			Files.write(p, pcmString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
        
       
        


		
	}
	
	@Test
	public void csv2() throws Exception {
		
		String csvFileName = "input-csv/erasmus.csv";
		List<PCMContainer> pcms = PCMUtil.loadCSV(csvFileName); // already interpreted with CellContentInterpreter
		PCMContainer pcmContainer = pcms.get(0);
		assertNotNull(pcmContainer);
		
		assertEquals(195, pcmContainer.getPcm().getProducts().size());
		
		KMFJSONExporter jsonExporter = new KMFJSONExporter();
        
		String pcmString = jsonExporter.export(pcmContainer);

		Path p = Paths.get("output/" + "erasmus.json");
		try {
			Files.write(p, pcmString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
        
       
        


		
	}

	@Test
	public void csvParrainages() throws Exception {

		String csvFileName = "input-csv/parrainagestotal.csv";
		List<PCMContainer> pcms = PCMUtil.loadCSV(new File(csvFileName), PCMDirection.PRODUCTS_AS_LINES, ';'); // already interpreted with CellContentInterpreter
		PCMContainer pcmContainer = pcms.get(0);
		assertNotNull(pcmContainer);

		assertEquals(14296, pcmContainer.getPcm().getProducts().size());

		KMFJSONExporter jsonExporter = new KMFJSONExporter();

		String pcmString = jsonExporter.export(pcmContainer);

		/*
		Path p = Paths.get("output/" + "parrainages.json");
		try {
			Files.write(p, pcmString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}*/






	}
	
}
