package misc;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data_off.PCMUtil;
import org.junit.Test;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;

public class PCMCompleter {
	
	

	@Test 
	public void completePCMProgrammingLanguage() throws Exception {
		PCMContainer pcmContainer = PCMUtil.loadPCMContainer("output/mergedPL.pcm");
		PCM pcm = pcmContainer.getPcm();
		PCMFactory pcmFactory = new PCMFactoryImpl();
		Map<String, String> product2Images = new PCMComplementerImageOfProduct().completeByProduct(pcm);
		System.err.println("product2Images found: " + product2Images);
				
		// add a column/feature
		Feature newFt = pcmFactory.createFeature();
		newFt.setName("imgProduct");
		pcm.addFeature(newFt);
		
		Set<String> pdts = product2Images.keySet();
		for (String pdtName : pdts) {
			Product pdt = pcm.getOrCreateProduct(pdtName, pcmFactory);
			
			Cell newCell = pcmFactory.createCell();				
			newCell.setFeature(newFt);
			newCell.setContent(product2Images.get(pdtName));
			newCell.setRawContent(product2Images.get(pdtName));	
			
			pdt.addCell(newCell);
		}
		
		KMFJSONExporter exporter = new KMFJSONExporter();
		String json = exporter.export(pcmContainer);
		String jsonFileName = "output/" + "mergedPL2.pcm";
		// Write modified PCM
		writeToFile("" + jsonFileName, json);
		
	}
	
	@Test 
	public void completePCMEuro2016() throws Exception {
		
		PCMComplementerImageOfProduct.LANGUAGE = "fr";
		PCMContainer pcmContainer = PCMUtil.loadPCMContainer("input-pcm/euro2016.pcm");
		PCM pcm = pcmContainer.getPcm();
		PCMFactory pcmFactory = new PCMFactoryImpl();
		Map<String, String> product2Images = new PCMComplementerImageOfProduct().completeByProduct(pcm);
		System.err.println("product2Images found: " + product2Images);
				
		// add a column/feature
		Feature newFt = pcmFactory.createFeature();
		newFt.setName("imgProduct");
		pcm.addFeature(newFt);
		
		Set<String> pdts = product2Images.keySet();
		for (String pdtName : pdts) {
			Product pdt = pcm.getOrCreateProduct(pdtName, pcmFactory);
			
			Cell newCell = pcmFactory.createCell();				
			newCell.setFeature(newFt);
			newCell.setContent(product2Images.get(pdtName));
			newCell.setRawContent(product2Images.get(pdtName));	
			
			pdt.addCell(newCell);
		}
		
		KMFJSONExporter exporter = new KMFJSONExporter();
		String json = exporter.export(pcmContainer);
		String jsonFileName = "output/" + "euro2016IMG.pcm";
		// Write modified PCM
		writeToFile("" + jsonFileName, json);
		
	}
	
	
	@Test 
	public void completePCMErasmus() throws Exception {
		
		String ftName = "Pays";
		PCMContainer pcmContainer = PCMUtil.loadPCMContainer("input-pcm/erasmus.pcm");
		PCM pcm = pcmContainer.getPcm();
		
		PCMFactory pcmFactory = new PCMFactoryImpl();
		Map<String, String> ft2Images = new PCMComplementerImageOfProduct().completeByFeature(pcm, ftName);
		System.err.println("product2Images found: " + ft2Images);
		
		
		
		
		// add a column/feature
		Feature newFt = pcmFactory.createFeature();
		newFt.setName("img" + ftName);
		pcm.addFeature(newFt);
		
		assertTrue(ft2Images.size() >= 1); // otherwise it means it is not a good feature name
		
	
		Feature ft = pcm.getOrCreateFeature(ftName, pcmFactory);
		List<Cell> cells = ft.getCells();
		
		// we iterate over cells of a given ft
		// for each value cell cell we create a new cell, whose value is in the map
		// for each new cell, we associate the corresponding product
		for (Cell cell : cells) {
						
			Product pdt = cell.getProduct();			
			String cellContent = cell.getContent();
			
			String newCellValue = ft2Images.containsKey(cellContent) ? ft2Images.get(cellContent) : "N/A";
						
			Cell newCell = pcmFactory.createCell();				
			newCell.setFeature(newFt);
			newCell.setContent(newCellValue);
			newCell.setRawContent(newCellValue);	
			
			pdt.addCell(newCell);
		}
		
		KMFJSONExporter exporter = new KMFJSONExporter();
		String json = exporter.export(pcmContainer);
		String jsonFileName = "output/" + "erasmusIMG.pcm";
		// Write modified PCM
		writeToFile("" + jsonFileName, json);
		
	}
	
	private void writeToFile(String path, String content) throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
		writer.write(content);
		writer.close();
	}

}
