import static org.junit.Assert.*;


import org.diverse.pcm.api.java.AbstractFeature;
import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Feature;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.WikipediaPageMiner;
import org.diverse.pcm.io.wikipedia.export.CSVExporter;
import org.diverse.pcm.io.wikipedia.export.HTMLExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Matrix;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import org.diverse.pcm.io.wikipedia.ParserTest;
import org.junit.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import java.io.*;

import static org.junit.Assert.*;
import static scala.collection.JavaConversions.*;

public class WikipediaPageMinerVerifPCM


{


    @Test
    public void test()
    {


        WikipediaPageMiner miner = new WikipediaPageMiner();


        // Parse article from Wikipedia
        String code = miner.getPageCodeFromWikipedia("Comparison_of_document_interfaces");
        String preprocessedCode = miner.preprocess(code);
        Page page = miner.parse(preprocessedCode);


        // HTML export
        HTMLExporter htmlExporter = new HTMLExporter();
        String html = htmlExporter.export(page);
        //System.out.println(html);
        assertNotNull(html);

        // CSV export
        CSVExporter csvExporter = new CSVExporter();
        String csv = csvExporter.export(page);
        //System.out.println(csv);
        assertNotNull(csv);

        // PCM model export
        PCMModelExporter pcmExporter = new PCMModelExporter();

        List<PCM> pcms = seqAsJavaList(pcmExporter.export(page));
        assertFalse(pcms.isEmpty());


     /*   System.out.println("***************************************products***************************************");
         for (Product p: pcm.getProducts()){
            System.out.println(p.getName()+" => "+p.getCell("memory efficiency").getContent());
        }*/
        System.out.println("***************************************Features***************************************");
        /**
         * This test returns the content of one cell for product
         */
       /* String nom_p = pcm.getProducts().get(0).getName();
        Cell p = pcm.getProducts().get(0).getCell("memory efficiency");
        System.out.println(nom_p+" "+p.getContent());
        for(AbstractFeature f : pcm.getFeatures()){
            System.out.println(f.getName());
        }*/

        /**
         *Phase d'exportation vers Output vers le format.PCM
         */
        //  ParserTest test_scala=new ParserTest();



        /**
         * Pour chaque produit, on doit afficher les features correspondants
         */

       /* for(Product p : pcm.getProducts()){
            System.out.println("***************************************products***************************************");
            System.out.println("product name: " + p.getName());
            for(Cell c: p.getCells()){
               // Feature f = c.getFeature();
               // Cell cs = p.getCell(f);
                System.out.println(c.getFeature().getName());
                System.out.println(c.getContent());
               // System.out.println(p.getCell(f));
               // System.out.println();
            }
        }*/

        for (PCM pcm : pcms)

        {

            for (AbstractFeature f : pcm.getFeatures()) {
                System.out.println(f.getName());
                for (Cell cs : ((Feature) f).getListFeature(pcm)) {
                     System.out.println("contenu => "+cs.getContent());
                }

            }
        }







    }






}