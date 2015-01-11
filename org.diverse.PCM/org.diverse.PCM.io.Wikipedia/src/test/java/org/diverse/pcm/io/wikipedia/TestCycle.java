package org.diverse.pcm.io.wikipedia;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporterOld;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Page;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.diverse.pcm.io.wikipedia.FileFunctions.*;
import static org.junit.Assert.*;
import static scala.collection.JavaConversions.*;

/**
 * Created by Hvallee on 10/01/2015.
 */
public class TestCycle {
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    String date = format.format(new Date());


    String filePath = "resources/list_of_PCMs2.txt";

    WikipediaPageMiner miner = new WikipediaPageMiner();
    ParserTest parser = new ParserTest();

    @org.junit.Test
    public void testCycle() {
        try {
            System.out.println("Test parse from Wikipedia to PCM");
            testWikiToPCM();
            System.out.println("Test PCM to Wikitext");
            //testPCMtoWiki();
            System.out.println("Test Wikitext to PCM");
            //testWikitextToPCM();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Test with a wikipedia sample with few own matrice */
    public void testWikiToPCM() {
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

                        writeRawPages(page,line);

                        // page export to pcm(html)
                        PCMModelExporterOld pcmExporter = new PCMModelExporterOld();
                        PCM pcm = pcmExporter.export(page);
                        assertNotNull(pcm);
                        parser.writeToPCMDaily(title, page);

                        // Enregistrement du PCM (JSON) créer un daily ?
                        parser.writeToPCM(line,page);

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


    /* Test pcm to wikitext with pcms(html) from previous test */
    public void testPCMtoWiki (){
        try {
            BufferedReader buff = new BufferedReader(new FileReader(filePath));
            try {
                String line;
                while ((line = buff.readLine()) != null) {

                    System.out.println(line);


                    // Parse article from Wikipedia
                    String code = miner.getPageCodeFromWikipedia(line);
                    String preprocessedCode = miner.preprocess(code);
                    Page page = miner.parse(preprocessedCode);


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

                }
            } finally {
                buff.close();
            }
        } catch (IOException ioe) {
            System.out.println("Erreur --" + ioe.toString());
        }
    }

         /* Test wikitext to pcm */
       public void testWikitextToPCM() throws IOException {
           String pathString = "../org.diverse.PCM.io.Wikipedia/output/wikitext_" + date;
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
                   System.out.print("Miner"+page.toString()+"FinMiner");
                   // PCM model export
                   PCMModelExporterOld pcmExporter = new PCMModelExporterOld();
                   PCM pcm = pcmExporter.export(page);
                   parser.writeFromWikitextToPCMDaily(title, page);
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
