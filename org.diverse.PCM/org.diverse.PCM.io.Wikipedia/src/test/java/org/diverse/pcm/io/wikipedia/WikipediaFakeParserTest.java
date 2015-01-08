package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.*;
import org.diverse.pcm.io.wikipedia.export.*;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.junit.Assert.*;
import static scala.collection.JavaConversions.*;

/*
 * PCM generations test class
 *
 *
 */
public class WikipediaFakeParserTest {

    private WikipediaPageMiner miner = new WikipediaPageMiner();
    private String resources_path = getCurrentFolderPath() + "/resources/";
    File file = new File(resources_path + "Comparison_test.txt");

    int nbFeatures = 200;
    int nbProducts = 500;
    String code;
    String preprocessedCode;
    Page page;
    List<PCM> pcms;

    /* Manage a file opening and returns its content
    
     */
    public String loadFile(File f) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        StringWriter out = new StringWriter();
        int b;
        while ((b=in.read()) != -1)
            out.write(b);
        out.flush();
        out.close();
        in.close();
        return out.toString();
    }

    private String getCurrentFolderPath() {
        String path = "";
        try {
            path = new java.io.File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
    
    /*
        Create a fake pcm nbFeatures*nbProducts
    
        This method manage the generation of a "fake" comparison file
       which is composed of product named "P1" and feature named "F1"
       The particularity is that cell's value is named "P1F1"
       which is quite simple to test
        
     */
    private void createBrutalPCM() throws IOException {

        FileWriter filew = new FileWriter(file);
        filew.write("{| class=\"wikitable sortable\"\n");
        for(int i=1; i <= nbFeatures; i++){
            filew.write("! F"+i+"\n");
        }
        filew.write("|-\n");

        for(int j=1; j <= nbProducts; j++){
            filew.write("|- P"+j);
            for(int i=1; i <= nbFeatures; i++){
                filew.write(" || P" + j + "F" + i);
            }
            filew.write("\n|-\n");
        }
        filew.write("|}");
        filew.close();
    }
    
    @Before
    public void setUp() throws Exception {
        // TODO: check for sonar to mesure performance
        createBrutalPCM();
        code = loadFile(file);
        preprocessedCode = miner.preprocess(code);
        long debut = System.currentTimeMillis();
        page = miner.parse(preprocessedCode);
        long fin = System.currentTimeMillis();
        System.out.println("TIME : " + (float) ((fin - debut)));
        PCMModelExporter pcmExporter = new PCMModelExporter();
        pcms = seqAsJavaList(pcmExporter.export(page));
    }

    @Test
    public void ParserTest() throws IOException {
        //HTML export
        HTMLExporter htmlExporter = new HTMLExporter();
        String html = htmlExporter.export(page);
        assertNotNull(html);

        // CSV export
        CSVExporter csvExporter = new CSVExporter();
        String csv = csvExporter.export(page);
        assertNotNull(csv);

        // PCM model export
        assertFalse(pcms.isEmpty());

        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)
        WikiTextExporter wikitextExporter = new WikiTextExporter();
        for (PCM pcm : pcms) {
            String wikitext = wikitextExporter.toWikiText(pcm);
            assertNotNull(wikitext);
        }
    }
    
    /* Test cells value as the way explained in createBrutalPCM's docstring
    
     */
    @Test
    public void CellsValueTest() {
        for (PCM pcm: pcms) {
            for (AbstractFeature feature: pcm.getFeatures()) {
                for (Product product: pcm.getProducts()) {
                    for (Cell cell: product.getCells()) {
                        assertEquals(feature.getName(), cell.getFeature().getName());
                        assertEquals(product.getName() + feature.getName(), cell.getContent());
                    }
                }

            }
        }
        
    }
}
