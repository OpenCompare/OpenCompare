package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporterOld;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Page;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.diverse.pcm.io.wikipedia.FileFunctions.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static scala.collection.JavaConversions.seqAsJavaList;

/**
 * Created by Hvallee on 10/01/2015.
 */
public class TestCycle {
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    String date = format.format(new Date());

    String filePath = "resources/list_of_PCMs2.txt";

    WikipediaPageMiner miner = new WikipediaPageMiner();
    ParserTest parser = new ParserTest();

    List<PCM> pcms1;

    @org.junit.Test
    public void testCycle() {
        try {
            System.out.println("Test parse from Wikipedia -> PCM (html) + PCM(JSON) -> Wikitext");
            testWikiToWikitext();
            System.out.println("Test Wikitext to PCM");
            testWikitextToPCM();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Test with a wikipedia sample with few own matrice */
    public void testWikiToWikitext() {
        try {
            BufferedReader buff = new BufferedReader(new FileReader(filePath));
                String line;
                String title;
            try {
                while ((line = buff.readLine()) != null) {
                    try {
                        title = line;
                        System.out.println(line);

                        // Parse article from Wikipedia
                        String code = miner.getPageCodeFromWikipedia(line);
                        String preprocessedCode = miner.preprocess(code);
                        writeToPreprocessed(preprocessedCode, title);
                        Page page = miner.parse(preprocessedCode);

                        // page export to pcm(html)
                        PCMModelExporterOld pcmExporter = new PCMModelExporterOld();
                        PCM pcm = pcmExporter.export(page);

                        assertNotNull(pcm);
                        parser.writeToPCMDailyHTML(title, page);

                        // Enregistrement du PCM (JSON) créer un daily ?
                        parser.writeToPCMDailyJSON(line, page);

                        //appel de la deuxième fonction
                       // testPCMtoWikitext(page, line);
                        // Export page as a List<PCM>
                        PCMModelExporter pcmExporterJSON = new PCMModelExporter();
                       pcms1 = seqAsJavaList(pcmExporterJSON.export(page));
                        assertFalse(pcms1.isEmpty());

                        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)
                        // Ajout du StringBuilder pour avoir un seul fichier
                        WikiTextExporter wikitextExporter = new WikiTextExporter();
                        StringBuilder wikitextFinal = new StringBuilder(line);
                        for (PCM pcmtowiki : pcms1) {

                            String wikitext = wikitextExporter.toWikiText(pcmtowiki);

                            wikitextFinal.append(wikitext);
                            //assertNotNull(wikitext);
                            // Sauvegarde du WikiText
                            parser.writeToWikiTextDaily(line, page);
                        }
                        writeWikitextForHTML(wikitextFinal, line, date);

                    } catch (Exception e) {
                        title = line;
                        appendToFile(e, title);
                        System.out.println("Error reported");
                    }
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    /* Test pcm to wikitext with pcms(html) from previous test
    public void testPCMtoWikitext(Page page, String line){
        // Export page as a List<PCM>
        PCMModelExporter pcmExporter = new PCMModelExporter();
        List<PCM> pcms = seqAsJavaList(pcmExporter.export(page));
        assertFalse(pcms.isEmpty());

        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)
        // Ajout du StringBuilder pour avoir un seul fichier
        WikiTextExporter wikitextExporter = new WikiTextExporter();
        StringBuilder wikitextFinal = new StringBuilder(line);
        for (PCM pcm : pcms) {
            String wikitext = wikitextExporter.toWikiText(pcm);
            wikitextFinal.append(wikitext);
            //assertNotNull(wikitext);
            // Sauvegarde du WikiText
            parser.writeToWikiTextDaily(line, page);
        }
        writeWikitextForHTML(wikitextFinal, line, date);
    }
 */

         /* Test wikitext to pcm */
       public void testWikitextToPCM() throws IOException {
           String pathString = "../org.diverse.PCM.io.Wikipedia/output/wikitext_" + date+"_full";

           Path path = Paths.get(pathString);
           File folder = new File(pathString);

           File[] listOfFiles = folder.listFiles();

           for (int i = 0; i < listOfFiles.length; i++) {

               if (listOfFiles[i].isFile()) try {
                   String title = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4);
                   System.out.println("File " + listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4));

                   // Parse article from Wikipedia
                   String file = path+"/"+listOfFiles[i].getName();
                   String code = readFile(file, Charset.defaultCharset());
                   String preprocessedCode = miner.preprocess(code);
                   Page page = miner.parse(preprocessedCode);

                   PCMModelExporter pcmExporterJSON = new PCMModelExporter();
                    List<PCM> pcms2 = seqAsJavaList(pcmExporterJSON.export(page));
                   int countPCM1 = 0;
                   ReaderPCMJSON readerpcm = new ReaderPCMJSON();
                   for(PCM pcm2 : pcms2){
                        PCM pcm1 = pcms1.get(countPCM1);
                       ArrayList<Product> produits =  (ArrayList)pcm1.getProducts();
                       assertTrue(readerpcm.containsAllProducts(produits, pcm2));
                       assertTrue(readerpcm.containsAllContents(produits, pcm2));
                       countPCM1++;
                   }

                   // Enregistrement du PCM (JSON) créer un daily ?
                   parser.writeToPCMDailyJSON2(title, page);

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
