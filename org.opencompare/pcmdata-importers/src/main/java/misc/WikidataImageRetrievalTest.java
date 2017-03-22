package misc;

import static org.junit.Assert.*;

import org.junit.Test;

public class WikidataImageRetrievalTest {
	
	
	@Test
	public void testScala() throws Exception {
		
		String urlScala = 
				new WikidataImageRetrieval().retrieve("Q460584", "P18");
		assertEquals("https://upload.wikimedia.org/wikipedia/commons/f/f8/Python_logo_and_wordmark.svg", urlScala);
				
	}
	
	@Test
	public void testCpp() throws Exception {
		String urlCpp = 
				new WikidataImageRetrieval().retrieve("Q2407", "P154");
		assertEquals("https://upload.wikimedia.org/wikipedia/commons/5/5b/C_plus_plus.svg", urlCpp);
			
		
	}
	
	@Test
	public void testJS() throws Exception {
		String urlJS = 
				new WikidataImageRetrieval().retrieve("Q2005", "P154");
		assertEquals("https://upload.wikimedia.org/wikipedia/commons/9/99/Unofficial_JavaScript_logo_2.svg", urlJS);
			
		
	}
	
	@Test
	public void testPython() throws Exception {
		String urlPython = new WikidataImageRetrieval().retrieve("Q28865", "P154");
		assertEquals("https://upload.wikimedia.org/wikipedia/commons/f/f8/Python_logo_and_wordmark.svg", urlPython);
	}
	
	@Test
	public void testJava() throws Exception {
		String urlJava = new WikidataImageRetrieval().retrieve("Q251", "P154");
		assertEquals("https://upload.wikimedia.org/wikipedia/commons/4/40/Wave.svg", urlJava);
	}
}
