package org.diverse.pcm.io.wikipedia;


import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporterOld;
import org.diverse.pcm.io.wikipedia.pcm.Page;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.diverse.pcm.io.wikipedia.FileFunctions.*;
import static org.junit.Assert.assertNotNull;

public class TestExport {
    File folder = new File("../org.diverse.PCM.io.Wikipedia/input");
    File[] listOfFiles = folder.listFiles();
    File folder2 = new File("../org.diverse.PCM.io.Wikipedia/inputTest");
    File[] listOfFiles2 = folder2.listFiles();

    /* Test avec les matrices de Wikipedia */
    @org.junit.Test
    public void test() throws IOException {
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
                    parser.writeToPCMOld(title, page);
                    assertNotNull(pcm);
                } catch (Exception e) {
                    String title = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4)+("\n\n");
                    appendToFile(e, title);
                    System.out.println("Erreur reportée");
                }
             else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }

        }


    }

    /* Test aves des matrices personnalisées */
    @org.junit.Test
    public void test2() throws IOException {
        for (int i = 0; i < listOfFiles2.length; i++) {
            WikipediaPageMiner miner = new WikipediaPageMiner();
            if (listOfFiles2[i].isFile()) try {
                String FILE_PATH = listOfFiles2[i].getPath();
                String title = listOfFiles2[i].getName().substring(0, listOfFiles2[i].getName().length() - 4);
                System.out.println("File " + listOfFiles2[i].getName().substring(0, listOfFiles2[i].getName().length() - 4));

                // Parse article from Wikipedia
                String code = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
                String preprocessedCode = miner.preprocess(code);
                writeToPreprocessed(preprocessedCode, title);

                Page page = miner.parse(preprocessedCode);

                // PCM model export
                PCMModelExporterOld pcmExporter = new PCMModelExporterOld();
                PCM pcm = pcmExporter.export(page);
                ParserTest parser = new ParserTest();
                parser.writeToPCMOld(title, page);
                assertNotNull(pcm);
            } catch (Exception e) {
                String title = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4)+("\n\n");
                appendToFile(e, title);
                System.out.println("Erreur reportée");
            }
            else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }

        }
    }
}
