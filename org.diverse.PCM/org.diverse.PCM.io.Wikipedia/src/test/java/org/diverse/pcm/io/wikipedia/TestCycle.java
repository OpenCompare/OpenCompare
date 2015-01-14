package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
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
import java.util.logging.Logger;

import static org.diverse.pcm.io.wikipedia.FileFunctions.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

    private static Logger logger = Logger.getLogger("TestCycle");

    StringBuilder report = new StringBuilder();

    @Before
    public void testCycle() {
        try {
           cycleGenerator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testSameProducts(){
        cycleGenerator();
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
            assertTrue(readerpcm.containsAllContents(pcm1, pcm2));
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

    /* Test with a wikipedia sample with few own matrice */
    public void cycleGenerator() {
        WikipediaPageMiner miner = new WikipediaPageMiner();
        ParserTest parser = new ParserTest();
        
        int OK = 0 , NOK = 0;
        try {
            BufferedReader buff = new BufferedReader(new FileReader(filePath));
            String line;
            String log;
            try {
                while ((line = buff.readLine()) != null) {
                    try {
                        log = "Wikipedia to PCM: " + line;
                        report.append(log); report.append("\n");
                        logger.info(log);

                        // Parse article from Wikipedia
                        String code = miner.getPageCodeFromWikipedia(line);
                        String preprocessedCode = miner.preprocess(code);
                        Page page = miner.parse(preprocessedCode);

                        // Enregistrement du PCM (JSON) créer un d
                        parser.writeToPCMDaily(line, page);
                        log = line+"PCM JSON Enregistré";
                        report.append(log); report.append("\n");
                        logger.info(log);

                        // Export page as a List<PCM>
                        PCMModelExporter pcmExporterJSON = new PCMModelExporter();
                        pcms1 = seqAsJavaList(pcmExporterJSON.export(page));
                        if(pcms1.isEmpty()){
                            log = "Liste JSON PCMs1 is Empty";
                            logger.warning(log);
                            report.append(log); report.append("\n");
                        }
                        else{
                            log = "Liste JSON PCMs1 is OK";
                            report.append(log); report.append("\n");
                            logger.info(log);
                        }
                        //assertFalse(pcms1.isEmpty());

                        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)
                        WikiTextExporter wikitextExporter = new WikiTextExporter();
                        for (PCM pcmtowiki : pcms1) {
                            String wikitext = wikitextExporter.toWikiText(pcmtowiki);
                            // WikitextSave
                            parser.writeToWikiTextDaily(line, page);
                        }

                        CycleGenerator2();
                        OK++;
                    } catch (Exception e) {
                        log = " Error : " + line + " "+ e.getMessage();
                        report.append(log);
                        logger.warning("Error :"+log);
                        NOK++;
                    }
                }
            }catch (Exception e) {
                log = " Error : "+ e.getMessage();
                report.append(log); report.append("\n");
                logger.warning("Error :"+log);
                NOK++;
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        System.out.println("OK="+OK+"NOK="+NOK);
        writeReporting(report);
        System.out.println(report);
    }

    /* Test wikitext to pcm */
    public void CycleGenerator2() throws IOException {
        WikipediaPageMiner miner = new WikipediaPageMiner();
        ParserTest parser = new ParserTest();
        String pathString = "output/dailyOutput/"+ date +"/wikitext" ;

        Path path = Paths.get(pathString);
        File folder = new File(pathString);

        File[] listOfFiles = folder.listFiles();
        
        String log;

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) try {
                String title = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4);
                log = "Wikitext to PCM -> File: " + listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4);
                logger.info(log);
                report.append(log); report.append("\n");
                
                String file = path+"/"+listOfFiles[i].getName();
                String code = readFile(file, Charset.defaultCharset());
                String preprocessedCode = miner.preprocess(code);
                Page page = miner.parse(preprocessedCode);
                parser.writeToPCMDaily2(title, page);

                PCMModelExporter pcmExporterJSON = new PCMModelExporter();
                pcms2 = seqAsJavaList(pcmExporterJSON.export(page));

                if(pcms1.isEmpty()){
                    log = "Liste JSON PCMs2 is Empty";
                    logger.warning(log);
                    report.append(log); report.append("\n");
                }
                else{
                    log = "Liste JSON PCMs2 is OK";
                    report.append(log); report.append("\n");
                    logger.info(log);
                }
                
            } catch (Exception e) {
                String title = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4)+("\n\n");
                log = " Error : " + title + " "+ e.getMessage();
                report.append(log); report.append("\n");
                logger.warning("Error :"+log);
            }
            else if (listOfFiles[i].isDirectory()) {
                logger.warning("Directory " + listOfFiles[i].getName());
            }

        }

    }

}
