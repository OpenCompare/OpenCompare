package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.api.java.impl.ProductImpl;
import org.diverse.pcm.io.wikipedia.WikipediaPageMiner;
import org.diverse.pcm.io.wikipedia.export.CSVExporter;
import org.diverse.pcm.io.wikipedia.export.HTMLExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Matrix;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.text.html.HTMLDocument;

import static scala.collection.JavaConversions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Created by Oumoul on 10/01/2015.
 */
public class ComparaisonPCMTest {
    
    private  WikipediaPageMiner miner;
    private  List<PCM> pcms0;
    private   List<PCM> pcms1;
    private Page page;
    private PCMModelExporter pcmExporter;
    private  WikiTextExporter wikitextExporter;
    
    @Before
    public void setUp()
    {
        miner = new WikipediaPageMiner();
        pcms0 = new ArrayList<PCM>();
        pcms1 = new ArrayList<PCM>();
        pcmExporter = new PCMModelExporter();
        wikitextExporter=new WikiTextExporter();
    }
    
    
    @Test
    public void test() 
    {
       

        // Parse article from Wikipedia
        String code = miner.getPageCodeFromWikipedia("Comparison_of_Asian_national_space_programs");
        String preprocessedCode = miner.preprocess(code);
         page = miner.parse(preprocessedCode);

        
        pcms0 = seqAsJavaList(pcmExporter.export(page));
        System.out.println("taille de la liste "+pcms0.size());
        assertFalse(pcms0.isEmpty());

        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)


        for (PCM pcm : pcms0) 
        {
            String wikitext = wikitextExporter.toWikiText(pcm);
            Page page2 = miner.parse(wikitext);
            List<PCM> p = seqAsJavaList(pcmExporter.export(page2));
            pcms1.addAll(p);
            assertNotNull(wikitext);
        }
        assertFalse(pcms1.isEmpty());
        //Checks that the  PCM lists are not empty.
        assertTrue("la liste PCM0 ne doit pas etre vide ", pcms0.size()>0);
        assertTrue("la liste PCM1 ne doit pas etre vide ", pcms1.size()>0);

        //Checks that the two compared PCM have the same size 
        assertEquals("les 2 listes ont la meme taille ",pcms0.size(),pcms1.size());

        //checks that a product in one compared PCM has the same features as the other one.
        for(int i =0; i<pcms0.size(); i++)
        {
            for(int j=0; j<pcms0.get(i).getProducts().size(); j++)
            {
                if((pcms0.get(i).getProducts().get(j).getName())==(pcms1.get(i).getProducts().get(j).getName()))
                {
                   
                    assertEquals("Les produits ont les memes features.",pcms0.get(i).getProducts().get(j).getCells(),pcms1.get(i).getProducts().get(j).getCells());
                    
                }
                
            }
        }
        

    }



}
