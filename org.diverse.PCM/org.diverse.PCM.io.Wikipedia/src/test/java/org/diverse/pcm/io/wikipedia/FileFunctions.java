package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.io.wikipedia.pcm.Page;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/* This is a class with useful function on files */
public class FileFunctions {


    static String readFile(String path, Charset encoding) throws IOException {

        StringBuilder string = new StringBuilder();

        BufferedReader buff = new BufferedReader(new FileReader(path));
        try {
            String line;
            while ((line = buff.readLine()) != null) {

                string.append(line+"\n");
            }
        } finally {
            buff.close();
        }
        String file = string.toString();
        return file;
    }

    public static void appendToFile(Exception e, String title) {
           SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
           String date = format.format(new Date());
        try {
            FileWriter fstream = new FileWriter("output/dailyOutput/"+ date +"reporting.txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            PrintWriter pWriter = new PrintWriter(out, true);
            pWriter.write("\n\n"+title+"\n");
            e.printStackTrace(pWriter);
            pWriter.close();
        }
        catch (Exception ie) {
            throw new RuntimeException("Could not write Exception to file", ie);
        }
    }

    public static void writeReporting(StringBuilder builder) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String date = format.format(new Date());
        try {
            FileWriter fstream = new FileWriter("output/dailyOutput/"+ date +"reporting.txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            PrintWriter pWriter = new PrintWriter(out, true);
            pWriter.write(builder.toString());
            pWriter.close();
        }
        catch (Exception ie) {
            throw new RuntimeException("Could not write Exception to file", ie);
        }
    }


    public static void writeToPreprocessed(String preprocessed, String title) {
        try {
            FileWriter fstream = new FileWriter("output/preprocessed/"+title+".txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            PrintWriter pWriter = new PrintWriter(out, true);
            pWriter.write(preprocessed);
            pWriter.close();
        }
        catch (Exception ie) {
            throw new RuntimeException("Could not write Exception to file", ie);
        }
    }

    public static void writeRawPages(Page page, String title) {
        try {
            FileWriter fstream = new FileWriter("output/raw_pages/"+title+".txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            PrintWriter pWriter = new PrintWriter(out, true);
            pWriter.write(page.toString());
            pWriter.close();
        }
        catch (Exception ie) {
            throw new RuntimeException("Could not write Exception to file", ie);
        }
    }

}
