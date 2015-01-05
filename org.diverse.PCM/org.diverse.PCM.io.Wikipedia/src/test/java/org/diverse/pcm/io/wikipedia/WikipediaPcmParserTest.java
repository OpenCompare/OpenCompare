package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.export.CSVExporter;
import org.diverse.pcm.io.wikipedia.export.HTMLExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static scala.collection.JavaConversions.*;

/*
 * PCM generations test class
 *
 *
 */
public class WikipediaPcmParserTest {

    private WikipediaPageMiner miner = new WikipediaPageMiner();
    private String resources_path = getCurrentFolderPath() + "/resources/";
    private String output_path = getCurrentFolderPath() + "/output/";

    private String getCurrentFolderPath() {
        String path = "";
        try {
            path = new java.io.File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

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

    public void createBrutalPCM() throws IOException {
        int nbFeatures = 200;
        int nbProducts = 4000;

        File file = new File(output_path + "Comparison_test_result.txt");
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

    @Test
    public void FakePcmTest() throws IOException {

        // Parse article from Wikipedia
        //String code = miner.getPageCodeFromWikipedia("Comparison_of_Nikon_DSLR_cameras");
        createBrutalPCM();
        File file = new File(resources_path + "Comparison_test.txt");
        String code = loadFile(file);
        String preprocessedCode = miner.preprocess(code);
        long debut = System.currentTimeMillis();
        Page page = miner.parse(preprocessedCode);
        long fin = System.currentTimeMillis();
        System.out.println("TIME : "+(float)((fin-debut)));
        assertTrue(500 > fin- debut);

        //HTML export
        HTMLExporter htmlExporter = new HTMLExporter();
        String html = htmlExporter.export(page);
        assertNotNull(html);

        // CSV export
        CSVExporter csvExporter = new CSVExporter();
        String csv = csvExporter.export(page);
        assertNotNull(csv);

        // PCM model export
        PCMModelExporter pcmExporter = new PCMModelExporter();
        List<PCM> pcms = seqAsJavaList(pcmExporter.export(page));
        assertFalse(pcms.isEmpty());

        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)
        WikiTextExporter wikitextExporter = new WikiTextExporter();
        for (PCM pcm : pcms) {
            String wikitext = wikitextExporter.toWikiText(pcm);
            assertNotNull(wikitext);
        }
    }
}
