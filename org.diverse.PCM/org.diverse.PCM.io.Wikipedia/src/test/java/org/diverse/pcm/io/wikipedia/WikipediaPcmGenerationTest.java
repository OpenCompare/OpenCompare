package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.impl.export.PCMtoJsonImpl;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Page;

import static org.junit.Assert.*;
import static scala.collection.JavaConversions.*;

import org.junit.Test;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/*
 * PCM generations test class
 *
 *
 */
public class WikipediaPcmGenerationTest {

    private WikipediaPageMiner miner = new WikipediaPageMiner();

    private String output_path = getCurrentFolderPath() + "/output/" + getCurrentFormattedDate() + "/";
    private String model_path = output_path + "model/";
    private String html_path = output_path + "html/";
    private String csv_path = output_path + "csv/";
    private String wikitext_path = output_path + "wikitext/";
    private String model_error_filepath = output_path + "model_generation_errors.csv";

    private Iterator<String> pcms = getPcmList();
    private String date = getCurrentFormattedDate();

    private String getCurrentFolderPath() {
        String path = "";
        try {
            path = new java.io.File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public Page parseFromTitle(String title) {
        String code = miner.getPageCodeFromWikipedia(title);
        String  preprocessedCode = miner.preprocess(code);
        return miner.parse(preprocessedCode);
    }

    public Boolean createFolder(String path) {
        File file = new File(path);
        return file.exists() || file.mkdirs();
    }

    public Boolean createFile(String path) throws IOException {
        File file = new File(path);
        return file.exists() || file.createNewFile();
    }

    private String getCurrentFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    private  void reportError(BufferedWriter buffer, String title, Exception except) {
        // Do not log excetion from report
        String line = "\"" + date + "\";\"" + title + "\";\"" + except.toString() + "\"";
        try {
            buffer.write(line);
            buffer.newLine();
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String writeToPCM(String title, Page page) throws IOException{
        String path = "";
        PCMModelExporter exporter = new PCMModelExporter();
        List<PCM> pcms = seqAsJavaList(exporter.export(page).seq());

        PCMtoJsonImpl serializer = new PCMtoJsonImpl();
        for (int index=0;index<pcms.size();index++) {
            PCM pcm = pcms.get(index);
            path = model_path + title.replaceAll(" ", "_") + "_" + index + ".pcm";
            FileWriter file = new FileWriter(path);
            file.write(serializer.toJson(pcm));
            file.close();
        }
        return path;
    }

    public String writeToWikitext(String title, Page page) throws IOException{
        String path = "";
        PCMModelExporter exporter = new PCMModelExporter();
        List<PCM> pcms = seqAsJavaList(exporter.export(page).seq());

        WikiTextExporter serializer = new WikiTextExporter();
        for (int index=0;index<pcms.size();index++) {
            PCM pcm = pcms.get(index);
            path = wikitext_path + title.replaceAll(" ", "_") + "_" + index + ".txt";
            FileWriter file = new FileWriter(path);
            file.write(serializer.toWikiText(pcm));
            file.close();
        }
        return path;
    }

    public String writeToHTML(String title, Page page) throws IOException{
        String path = html_path + title.replaceAll(" ", "_") + ".html";
        FileWriter file = new FileWriter(path);
        file.write(page.toHTML().toString());
        file.close();
        return path;
    }

    public String writeToCSV(String title, Page page) throws IOException{
        String path = csv_path + title.replaceAll(" ", "_") + ".csv";
        FileWriter file = new FileWriter(path);
        file.write(page.toCSV());
        file.close();
        return path;
    }

    public Iterator<String> getPcmList() {

        String pcm_file_list = "/resources/list_of_PCMs.txt";
        File existing_file = new File(pcm_file_list);
        assertFalse(existing_file.exists());
        BufferedReader file = null;
        try {
            file = new BufferedReader(new FileReader(getCurrentFolderPath() + pcm_file_list));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file != null ? file.lines().iterator() : null;
    }

    @Test
    public void CheckPcmsList() {
        assertTrue(pcms.hasNext());
    }

    @Test
    public void GeneratePcms() throws IOException {

        // This test is only done on error's generation file creation
        assertTrue(createFolder(output_path));
        assertTrue(createFolder(model_path));
        assertTrue(createFolder(html_path));
        assertTrue(createFolder(wikitext_path));
        assertTrue(createFolder(csv_path));
        assertTrue(createFile(model_error_filepath));

        BufferedWriter buffer = new BufferedWriter(new FileWriter(model_error_filepath));

        while (pcms.hasNext()) {
            String title = pcms.next();
            System.out.print("Traitement du PCM '" + title + "'");

            try {
                Page page = parseFromTitle(title);
                if (page.getMatrices().isEmpty()) {
                    throw new Exception("Empty matrices");
                }
                writeToPCM(title, page);
                writeToHTML(title, page);
                writeToCSV(title, page);
                writeToWikitext(title, page);
                System.out.println(": Done.");
            } catch (Exception e) {
                // In all case, log the results of any kind exceptions
                System.out.println(": Error !");
                reportError(buffer, title, e);
            }
        }
        buffer.close();
    }
}
