package misc;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.junit.Test;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.extractor.CellContentInterpreter;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.io.CSVLoader;
import org.opencompare.api.java.io.PCMDirection;

import data_omdb.ConformanceProduct;
import data_omdb.OMDBCSVProductFactory;
import data_omdb.OMDBMediaType;
import data_omdb.OMDBProduct;
import data_omdb.OMDBToProduct;




public class OMDBTest {
	
	@Test 
	public void testOMDBFilms1() throws IOException, JSONException {
		String h = OMDBCSVProductFactory.getInstance().mkHeaders(OMDBMediaType.MOVIE);
		String m = new OMDBToProduct().mkCSV(OMDBMediaType.MOVIE);
		
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter 
				(new FileOutputStream("output/data_omdb_film.csv"), 
						StandardCharsets.UTF_8));
		writer.write(h + System.getProperty("line.separator") + m);
		writer.close();
	}
	
	@Test 
	public void testOMDBSeries1() throws IOException, JSONException {
		String h = OMDBCSVProductFactory.getInstance().mkHeaders(OMDBMediaType.SERIES);
		String m = new OMDBToProduct().mkCSV(OMDBMediaType.SERIES);
		assertTrue(m.split("\r\n|\r|\n").length > 0);
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter 
				(new FileOutputStream("output/data_omdb_serie.csv"), 
						StandardCharsets.UTF_8));
		writer.write(h + System.getProperty("line.separator") + m);
		writer.close();
	}
	
	@Test 
	public void testOMDBEpisodes1() throws IOException, JSONException {
		String h = OMDBCSVProductFactory.getInstance().mkHeaders(OMDBMediaType.EPISODE);
		String m = new OMDBToProduct().mkCSV(OMDBMediaType.EPISODE);
		
		
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter 
				(new FileOutputStream("output/data_omdb_episode.csv"), 
						StandardCharsets.UTF_8));
		writer.write(h + System.getProperty("line.separator") + m);
		writer.close();
	}
	
	@Test 
	public void test1() throws IOException {
			
			// create the CSV
			//new OmdbtoProduct().mk... 
		
			// read the CSV file
			File file = new File("output/data_omdb_episode.csv");
			CSVLoader csvL = new CSVLoader(
	                new PCMFactoryImpl(),
	                new CellContentInterpreter(new PCMFactoryImpl()), 
	                PCMDirection.PRODUCTS_AS_LINES);
	        List<PCMContainer> pcmC = csvL.load(file);
	        PCM pcm = pcmC.get(0).getPcm();
			
			// check some properties of "pcm"
			assertNotNull(pcm);		
	}
	
	
	@Test 
	public void test3() throws IOException, JSONException {
		System.err.println("" + 
				new OMDBToProduct().mkCSVs(Arrays.asList(OMDBMediaType.SERIES)));
	}
	
	@Test 
	public void test4() throws IOException, JSONException {
		System.err.println("" + 
				new OMDBToProduct().mkCSVs(Arrays.asList(OMDBMediaType.MOVIE)));
	}
	
	@Test 
	public void test5() throws IOException, JSONException {
		System.err.println("" + 
				new OMDBToProduct().mkCSVs(Arrays.asList(OMDBMediaType.EPISODE)));
	}
	
	@Test 
	public void test6() throws IOException, JSONException {
		Map<OMDBMediaType, String> csvs = new OMDBToProduct().mkCSVs(Arrays.asList(
				OMDBMediaType.SERIES, 
				OMDBMediaType.EPISODE,
				OMDBMediaType.MOVIE
				));
		
		CSVLoader csvL = new CSVLoader(
                new PCMFactoryImpl(),
                new CellContentInterpreter(new PCMFactoryImpl()), 
                PCMDirection.PRODUCTS_AS_LINES);
        List<PCMContainer> pcmC = csvL.load(csvs.get(OMDBMediaType.MOVIE));
        PCM pcm = pcmC.get(0).getPcm();
        assertNotNull(pcm);
        
        int nbProduct1 = csvs.get(OMDBMediaType.MOVIE).split("\r\n|\r|\n").length;
        
        assertEquals(nbProduct1 - 1, pcm.getProducts().size());
      	
        
        CSVLoader csvL2 = new CSVLoader(
                new PCMFactoryImpl(),
                new CellContentInterpreter(new PCMFactoryImpl()), 
                PCMDirection.PRODUCTS_AS_LINES);
        List<PCMContainer> pcmC2 = csvL2.load(csvs.get(OMDBMediaType.EPISODE));
        PCM pcm2 = pcmC2.get(0).getPcm();
        assertNotNull(pcm2);
        
        int nbProduct2 = csvs.get(OMDBMediaType.EPISODE).split("\r\n|\r|\n").length;
        
        
        assertEquals(nbProduct2 - 1, pcm2.getProducts().size());
        
        CSVLoader csvL3 = new CSVLoader(
                new PCMFactoryImpl(),
                new CellContentInterpreter(new PCMFactoryImpl()), 
                PCMDirection.PRODUCTS_AS_LINES);
        List<PCMContainer> pcmC3 = csvL3.load(csvs.get(OMDBMediaType.SERIES));
        PCM pcm3 = pcmC3.get(0).getPcm();
        assertNotNull(pcm3);
        
        int nbProduct3 = csvs.get(OMDBMediaType.SERIES).split("\r\n|\r|\n").length;
        
        assertEquals(nbProduct3 - 1, pcm3.getProducts().size());
        assertTrue((nbProduct1 + nbProduct2 + nbProduct3) <= OMDBToProduct.NUMBER_OF_OMDB_PRODUCTS);
      	
        
        
		
	}
	
	@Test 
	public void testMovies() throws IOException, JSONException {
		
		CSVLoader csvL = new CSVLoader(
                new PCMFactoryImpl(),
                new CellContentInterpreter(new PCMFactoryImpl()), 
                ';', '"',
                PCMDirection.PRODUCTS_AS_LINES);
		
		String h = OMDBCSVProductFactory.getInstance().mkHeaders(OMDBMediaType.MOVIE);
		String m = 
				new OMDBToProduct(new ConformanceProduct() {
					
					@Override
					public boolean isOK(OMDBProduct pro) {
						return !pro.getPoster().equals("N/A") 
								&& !pro.getImdbRating().equals("N/A") 
								&& !pro.getImdbVotes().equals("N/A");
					}
				}).mkCSV(OMDBMediaType.MOVIE);

		String csv = h + System.getProperty("line.separator") + m;
		System.err.println("CSV: " + csv);
        List<PCMContainer> pcmC = csvL.load(csv);
        PCMContainer pcmContainer = pcmC.get(0);
        PCM pcm = pcmContainer.getPcm();
        assertNotNull(pcm);
        int nbProduct1 = csv.split("\r\n|\r|\n").length;
        assertEquals(nbProduct1 - 1, pcm.getProducts().size());
        
        
        int NB_HEADERS = 15;
        assertEquals(NB_HEADERS, pcm.getFeatures().size());
        List<Product> pdts = pcm.getProducts();
        for (Product product : pdts) {
			assertEquals(NB_HEADERS, product.getCells().size());
		}
        
        // serialize PCM to JSON (.pcm format)
        
        KMFJSONExporter pcmExporter = new KMFJSONExporter();
		String pcmString = pcmExporter.export(pcmC.get(0));

		Path p = Paths.get("output/" + "movies.json");
		try {
			Files.write(p, pcmString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
    
        
        
		
	}
	
	@Test 
	public void testSeries() throws IOException, JSONException {
		
		CSVLoader csvL = new CSVLoader(
                new PCMFactoryImpl(),
                new CellContentInterpreter(new PCMFactoryImpl()), 
                ';', '"',
                PCMDirection.PRODUCTS_AS_LINES);
		
		String h = OMDBCSVProductFactory.getInstance().mkHeaders(OMDBMediaType.SERIES);
		String m = new OMDBToProduct().mkCSV(OMDBMediaType.SERIES);

		String csv = h + System.getProperty("line.separator") + m;
		System.err.println("CSV: " + csv);
        List<PCMContainer> pcmC = csvL.load(csv);
        PCMContainer pcmContainer = pcmC.get(0);
        PCM pcm = pcmContainer.getPcm();
        assertNotNull(pcm);
        int nbProduct1 = csv.split("\r\n|\r|\n").length;
        assertEquals(nbProduct1 - 1, pcm.getProducts().size());
        
        
        int NB_HEADERS = 15;
        assertEquals(NB_HEADERS, pcm.getFeatures().size());
        List<Product> pdts = pcm.getProducts();
        for (Product product : pdts) {
			assertEquals(NB_HEADERS, product.getCells().size());
		}
        
        // serialize PCM to JSON (.pcm format)
        
        KMFJSONExporter pcmExporter = new KMFJSONExporter();
		String pcmString = pcmExporter.export(pcmC.get(0));

		Path p = Paths.get("output/" + "series.json");
		try {
			Files.write(p, pcmString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
    
        
        
		
	}@Test 
	public void testEpisodes() throws IOException, JSONException {
		
		CSVLoader csvL = new CSVLoader(
                new PCMFactoryImpl(),
                new CellContentInterpreter(new PCMFactoryImpl()), 
                ';', '"',
                PCMDirection.PRODUCTS_AS_LINES);
		
		String h = OMDBCSVProductFactory.getInstance().mkHeaders(OMDBMediaType.EPISODE);
		String m = new OMDBToProduct().mkCSV(OMDBMediaType.EPISODE);

		String csv = h + System.getProperty("line.separator") + m;
		System.err.println("CSV: " + csv);
        List<PCMContainer> pcmC = csvL.load(csv);
        PCMContainer pcmContainer = pcmC.get(0);
        PCM pcm = pcmContainer.getPcm();
        assertNotNull(pcm);
        int nbProduct1 = csv.split("\r\n|\r|\n").length;
        assertEquals(nbProduct1 - 1, pcm.getProducts().size());
        
        
        int NB_HEADERS = 18;
        assertEquals(NB_HEADERS, pcm.getFeatures().size());
        List<Product> pdts = pcm.getProducts();
        for (Product product : pdts) {
			assertEquals(NB_HEADERS, product.getCells().size());
		}
        
        // serialize PCM to JSON (.pcm format)
        
        KMFJSONExporter pcmExporter = new KMFJSONExporter();
		String pcmString = pcmExporter.export(pcmC.get(0));

		Path p = Paths.get("output/" + "episodes.json");
		try {
			Files.write(p, pcmString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
    
        
        
		
	}

}
