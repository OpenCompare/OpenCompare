import org.junit.Before;
import org.junit.Test;


import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.WikipediaPageMiner;
import org.diverse.pcm.io.wikipedia.export.CSVExporter;
import org.diverse.pcm.io.wikipedia.export.HTMLExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Matrix;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import org.diverse.pcm.io.wikipedia.ParserTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import java.io.*;




/**
 * Cette classe permet la création des PCMS1 à partir de Wikitest
 */
public class WikiTestPCM1 extends ParsingAutomatisationTest 
{
    private Page page;
    private WikipediaPageMiner miner;
    private ParserTest parser_PCM;

    @Before
    public void setUp()
    {
        miner = new WikipediaPageMiner();
        parser_PCM=new ParserTest();
        page=null;
    }
    
    
    @Test
public void test()
{
    
    String[] list_file = null;
    String ligne_file = null;
    String Edit_Code = null;
    String prepossed_code = null;
    StringBuffer edit;

    String link = System.getProperty("user.dir") + "\\output\\wikitext";
    File repertoire = new File(link);
    
    if (repertoire.isDirectory()) 
    {

        list_file = repertoire.list();
    }
    Arrays.sort(list_file);
    
    /**
     * Parcours du Repertoire
     *Trouver les .text et parsez-les
     */
    BufferedReader lire_code_PCM = null;

    for (int i = 0; i < list_file.length; i++)
    {
        String temp_file = list_file[i];

        System.out.println(temp_file);

        try {
            lire_code_PCM = new BufferedReader(new FileReader(link + "\\" + temp_file));

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }


        if (temp_file.endsWith(".txt") == true)
            try {
                while ((ligne_file = lire_code_PCM.readLine()) != null)
                {
                    Edit_Code +=ligne_file+"\n";
                }

                StringBuffer new_Title=new StringBuffer(temp_file);
                int k=new_Title.indexOf(".txt");
                new_Title.delete(k, temp_file.length());


                edit = new StringBuffer(Edit_Code);
                edit.delete(0,4);

                String test=miner.preprocess(edit.toString());
                page = miner.parse(test);

                try 
                {

                    ParserTest parser_PCM = new ParserTest();
                    parser_PCM.writeToPCM(new_Title.toString(), page);
                }
                catch (Exception e)
                {
                   ReportedCsvFile(new_Title.toString(),e.getMessage());
                    
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }

        Edit_Code=null;
        edit=null;
    }

/**
 * Couper-Coller 
 */
 String Destination_path=System.getProperty("user.dir")+"\\PCM1";
 String Source_Path=System.getProperty("user.dir")+"\\output\\model";
    PasteFile(Source_Path,Destination_path);

  }

}