package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static scala.collection.JavaConversions.seqAsJavaList;

/**
 * Created by StephaneMangin on 07/01/15.
 */
public class WikipediaReverseGenerationTest {


    private WikipediaPageMiner miner = new WikipediaPageMiner();
    private String resources_path = getCurrentFolderPath() + "/resources/";
    private File file = new File(resources_path + "Comparison_test.txt");

    private String code;
    private String preprocessedCode;
    private Page page;
    private PCMModelExporter pcmExporter;
    private WikiTextExporter wikitextExporter;
    private Map<PCM, String> pcmsMap;
    private List<PCM> pcms0;
    private List<PCM> pcms1;

    private String getCurrentFolderPath() {
        String path = "";
        try {
            path = new java.io.File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /* Manage a file opening and returns its content
    
     */
    private String loadFile(File f) throws IOException {
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

    @Test
    public void ReverseWikitextTest() {
        try {
            code = loadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        preprocessedCode = miner.preprocess(code);
        page = miner.parse(preprocessedCode);
        pcmExporter = new PCMModelExporter();
        wikitextExporter = new WikiTextExporter();
        pcms0 = seqAsJavaList(pcmExporter.export(page));
        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)
        for (PCM pcm : pcms0) {
            pcmsMap.put(pcm, wikitextExporter.toWikiText(pcm));
        }
        System.out.println(pcmsMap.keySet().toString());
        List<PCM> pcms1 = new ArrayList<PCM>();
        for (Object key: pcmsMap.entrySet()) {
            preprocessedCode = miner.preprocess(pcmsMap.get(key));
            page = miner.parse(preprocessedCode);
            pcms1.add(pcmExporter.export(page).head());
        }
        assertEquals(pcms0, pcms1);
        
    }
}
