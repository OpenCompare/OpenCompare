package misc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.PCMMetadata;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.io.CSVExporter;

public class CSVMerger {

	private static Logger _log = Logger.getLogger("CSVMerger");
	
	@Test
	public void testReadCSVs() throws Exception {
		File dirCSV = new File("/Users/macher1/Downloads/gh-emotional-commits-master");
		File[] csvS = dirCSV.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().startsWith("merged_counts_");
			}
		});
		
		
		PCMFactory pcmFactory = new PCMFactoryImpl();
		PCMContainer newPcmContainer = new PCMContainer();
		PCM newPcm = pcmFactory.createPCM();
		newPcmContainer.setPcm(newPcm);
		PCMMetadata metaData = new PCMMetadata(newPcm);
		newPcmContainer.setMetadata(metaData);
		
		
		Feature primaryFt = pcmFactory.createFeature();
		primaryFt.setName("Programming Language");
		newPcm.setProductsKey(primaryFt);
		newPcm.addFeature(primaryFt);
		
		
		for (File csv : csvS) {
			List<PCMContainer> pcms = PCMUtil.loadCSV(csv); // already interpreted with CellContentInterpreter
			PCMContainer pcmContainer = pcms.get(0);
			assertNotNull(pcmContainer);
			PCM pcm = pcmContainer.getPcm();
			assertNotNull(pcm);
			
			/*
			List<Feature> fts = pcm.getConcreteFeatures();
			Feature cFt = null; 
			for (Feature ft : fts) {
				if (ft.getName().equals("language"))
					cFt = ft;
			}
			assertNotNull(cFt);
			pcm.setProductsKey(cFt);
			cFt.setName("Programming Language");
			*/
			// pcm.getProductsKey().setName("Programming Language");
			
			
			
			
			_log.info("key= " + pcm.getProductsKey());
			assertTrue(11 == pcm.getProducts().size() || 12 == pcm.getProducts().size());
			String ftName = csv.getName().substring("merged_counts_".length()).replace(".csv", "");
			_log.info(ftName);
			
			/*
			Feature ftToRename = pcm.getConcreteFeatures().get(1);
			ftToRename.setName(ftName);
			
			
			if (newPcmContainer == null) {
				newPcmContainer = pcmContainer;
			}
			
			if (newPcm == null) 
				newPcm = pcm;
			else 
				newPcm.merge(pcm, pcmFactory);
				*/
		
			
			Feature newFt = pcmFactory.createFeature();
			newFt.setName(ftName);
			newPcm.addFeature(newFt);	
						
			List<Feature> fts = pcm.getConcreteFeatures();
			
			_log.info("fts= " + fts);
			int i = 0;
			int position = 0;
			for (Feature ft : fts) {
									
//				if (ft == pcm.getProductsKey())
//					continue;
				if (ft.getName().equals("language"))
					continue;
				//List<Cell> cells = ft.getCells();
				
				for (Product p : pcm.getProducts()) {
					
					Cell cell  = p.findCell(ft);
				//}
				
				//for (Cell cell : cells) {
					i++;
					//cell.getFeature()
					Cell newCell = pcmFactory.createCell();				
					newCell.setFeature(newFt);
					newCell.setContent(cell.getContent());
					newCell.setRawContent(cell.getContent());					
					//newProduct.getCells().add(newCell);
					
					// Product tProduct = p; // cell.getProduct();
					
					
					/*
					 Cell cellProduct = pcmFactory.createCell();
		             cellProduct.setContent(tProduct.getKeyCell().getContent());
		             cellProduct.setRawContent(tProduct.getKeyCell().getRawContent());
		             cellProduct.setInterpretation(tProduct.getKeyCell().getInterpretation());
		             cellProduct.setFeature(primaryFt);
		             
		             
		             tProduct.addCell(cellProduct);*/
		             
					
					// newPcm.setProductsKey(primaryFt);
					assertNotNull(p);
					_log.info("product: " + p.getKeyContent() + " feature=" + ft + " csv=" + csv.getName());
					
					/*
					Product newProduct = null;
					boolean found = false; 
					List<Product> productsAlreadyIn = newPcm.getProducts();
			        for (Product pdtAlreadyIn : productsAlreadyIn) {
			            if (pdtAlreadyIn.getKeyContent().equals(tProduct.getKeyContent())) {
			                newProduct = pdtAlreadyIn;
			            		found = true;
			            }
			        }
					
			        if (!found) {
			        		newProduct = pcmFactory.createProduct();
			        		newPcm.addProduct(newProduct);
			        		 Cell cellProduct = pcmFactory.createCell();
			             cellProduct.setContent(tProduct.getKeyCell().getContent());
			             cellProduct.setRawContent(tProduct.getKeyCell().getRawContent());
			             cellProduct.setInterpretation(tProduct.getKeyCell().getInterpretation());
			             cellProduct.setFeature(primaryFt);
			             
			             
			             newProduct.addCell(cellProduct);
			        		// assertNotNull(newProduct.findCell(primaryFt));
			            // newProduct.getKeyCell().setFeature(primaryFt);
			        		
			             
			        			
			        		assertNotNull(newProduct);
			        }*/
					
					boolean found = false; 
					List<Product> productsAlreadyIn = newPcm.getProducts();
			        for (Product pdtAlreadyIn : productsAlreadyIn) {
			            if (pdtAlreadyIn.getKeyContent().equals(p.getKeyContent())) {
			                found = true;
			            }
			        }
					
			        Product newProduct = 
							newPcm.getOrCreateProduct(p.getKeyContent(), pcmFactory);
			        
			        metaData.setProductPosition(newProduct, position++);
			        
			        if (!found) {
			        		Cell cellProduct = pcmFactory.createCell();
			             cellProduct.setContent(p.getKeyCell().getContent());
			             cellProduct.setRawContent(p.getKeyCell().getRawContent());
			             cellProduct.setInterpretation(p.getKeyCell().getInterpretation());
			             cellProduct.setFeature(primaryFt);
			             newProduct.addCell(cellProduct);
			        }
	        
					
			         newProduct.addCell(newCell);
			         
					
					
					
				}
				_log.info("#cells " + i);
				
			}
	
			
		}
		
		_log.info("#products:" + newPcm.getProducts().size());
		_log.info("#features: " + newPcm.getFeatures().size());
		
		_log.info("#cells: " + computeNbCells(newPcm));
		
		newPcm.setName("merged");
		
				
		
		// _log.info("csv: " + new CSVExporter().export(newPcmContainer));
		
		KMFJSONExporter exporter = new KMFJSONExporter();
		String json = exporter.export(newPcmContainer);
		String jsonFileName = "output/" + "mergedPL.pcm";
		// Write modified PCM
		writeToFile("" + jsonFileName, json);
		
	}
	
	
	private void writeToFile(String path, String content) throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
		writer.write(content);
		writer.close();
	}

	private int computeNbCells(PCM newPcm) {
		List<Product> pdts = newPcm.getProducts();
		int c = 0;
		for (Product pdt : pdts) {
			c += pdt.getCells().size();
		}
		return c;
	}
}
