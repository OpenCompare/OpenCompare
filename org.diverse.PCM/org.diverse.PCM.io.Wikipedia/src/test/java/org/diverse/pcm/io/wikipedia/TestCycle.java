package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporterOld;
import org.diverse.pcm.io.wikipedia.pcm.Page;

import java.io.File;
import java.io.IOException;

import static org.diverse.pcm.io.wikipedia.FileFunctions.appendToFile;
import static org.diverse.pcm.io.wikipedia.FileFunctions.writeToPreprocessed;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hvallee on 10/01/2015.
 */
public class TestCycle {
    File folder = new File("../org.diverse.PCM.io.Wikipedia/inputSample");
    File[] listOfFiles = folder.listFiles();


    /* Test with a wikipedia sample with few own matrice */
    @org.junit.Test
    public void testWikiToPCM() throws IOException {
        WikipediaPageMiner miner = new WikipediaPageMiner();


        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) try {
                String title = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4);
                System.out.println("File " + listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4));

                // Parse article from Wikipedia
                String code = miner.getPageCodeFromWikipedia(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4));
                String preprocessedCode = miner.preprocess(code);
                writeToPreprocessed(preprocessedCode, title);

                Page page = miner.parse(preprocessedCode);
                // PCM model export
                PCMModelExporterOld pcmExporter = new PCMModelExporterOld();
                PCM pcm = pcmExporter.export(page);
                ParserTest parser = new ParserTest();
                parser.writeToPCMDaily(title, page);
                assertNotNull(pcm);
            } catch (Exception e) {
                String title = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4)+("\n\n");
                appendToFile(e, title);
                System.out.println("Error reported");
            }
            else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }

        }


    }
}
