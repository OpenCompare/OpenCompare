package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporterOld;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.diverse.pcm.io.wikipedia.FileFunctions.*;
import static org.junit.Assert.*;
import static scala.collection.JavaConversions.seqAsJavaList;

/**
 * Created by Hvallee on 10/01/2015.
 */
public class TestCycle {
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    String date = format.format(new Date());
    String filePath = "resources/list_of_PCMs2.txt";
    List<PCM> pcms1;
    List<PCM> pcms2;

    @Before
    public void testCycle() {
        try {
           cycleGenerator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Test with a wikipedia sample with few own matrice */
    public void cycleGenerator() {
        WikipediaPageMiner miner = new WikipediaPageMiner();
        ParserTest parser = new ParserTest();
        try {
            BufferedReader buff = new BufferedReader(new FileReader(filePath));
                String line;
                String title;
            try {
                while ((line = buff.readLine()) != null) {
                    try {
                        title = line;
                        System.out.println("Wikipedia to PCM: "+line);

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

                        CycleGenerator2();
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

         /* Test wikitext to pcm */
       public void CycleGenerator2() throws IOException {
           WikipediaPageMiner miner = new WikipediaPageMiner();
           ParserTest parser = new ParserTest();
           String pathString = "../org.diverse.PCM.io.Wikipedia/output/wikitext_" + date;

           Path path = Paths.get(pathString);
           File folder = new File(pathString);

           File[] listOfFiles = folder.listFiles();

           for (int i = 0; i < listOfFiles.length; i++) {


               if (listOfFiles[i].isFile()) try {
                   String title = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4);
                   System.out.println("Wikitext to PCM -> File: " + listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4));
                   // Parse article from Wikipedia
                   String file = path+"/"+listOfFiles[i].getName();
                   String code = readFile(file, Charset.defaultCharset());
                   String preprocessedCode = miner.preprocess(code);
                   Page page = miner.parse(preprocessedCode);
                   writeToPreprocessed(preprocessedCode, title);
                   parser.writeToPCMDailyJSON2(title, page);

                   PCMModelExporter pcmExporterJSON = new PCMModelExporter();
                    pcms2 = seqAsJavaList(pcmExporterJSON.export(page));

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

    @org.junit.Test
         public void testSameProducts(){
        int countPCM1 = 0;
        ReaderPCMJSON readerpcm = new ReaderPCMJSON();
        for(PCM pcm2 : pcms2){
            PCM pcm1 = pcms1.get(countPCM1);
            assertTrue(readerpcm.containsAllProducts(pcm1, pcm1));
            countPCM1++;
        }
    }

    @org.junit.Test
    public void testSameContents(){
        int countPCM1 = 0;
        ReaderPCMJSON readerpcm = new ReaderPCMJSON();
        for(PCM pcm2 : pcms2){
            PCM pcm1 = pcms1.get(countPCM1);
            List<Product> produits =  pcm1.getProducts();
            assertTrue(readerpcm.containsAllContents(produits, pcm2));
            countPCM1++;
        }
    }

    @org.junit.Test
    public void testOrderProducts(){
        int countPCM1 = 0;
        ReaderPCMJSON readerpcm = new ReaderPCMJSON();
        for(PCM pcm2 : pcms2){
            PCM pcm1 = pcms1.get(countPCM1);
            int repeatTests = 0;
            while(repeatTests < 20){
                assertTrue(readerpcm.sameRandomProduct(pcm1, pcm2));
                repeatTests++;
            }
            countPCM1++;
        }
    }


    @org.junit.Test
    public void testOrderCells(){
        int countPCM1 = 0;
        ReaderPCMJSON readerpcm = new ReaderPCMJSON();
        for(PCM pcm2 : pcms2){
            PCM pcm1 = pcms1.get(countPCM1);
            int repeatTests = 0;
            while(repeatTests < 20){
                assertTrue(readerpcm.sameRandomCell(pcm1, pcm2));
                repeatTests++;
            }
            countPCM1++;
        }
    }


}
