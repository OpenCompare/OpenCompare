import static org.junit.Assert.*;


import org.diverse.pcm.api.java.*;
import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.impl.CellImpl;
import org.diverse.pcm.io.wikipedia.WikipediaPageMiner;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.*;
import org.diverse.pcm.io.wikipedia.pcm.*;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import org.diverse.pcm.io.wikipedia.ParserTest;
import org.junit.Test;
import scala.reflect.internal.AnnotationInfos;




import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertNotNull;



public class WikipediaPageMinerGeneratePCM

{

    @Test
    public void test() throws IOException
    {

/**
 * Phase de recuperation de tous les fichiers à tester dans la list_PCMs.text
 */
        int i = 0;
        String ligne = null;
        List<String>fichier_PCM=new ArrayList<String>();
        String[]tab_PCM=null;

        String path = System.getProperty("user.dir");

        path += "\\resources\\list_of_PCMs.txt";


        BufferedReader lire_Fichier = new BufferedReader(new FileReader(path));
        if (lire_Fichier == null) throw new FileNotFoundException("Fichier non trouvé: " + path);

        else
        {


            while ((ligne=lire_Fichier.readLine())!=null)
            {
                i++;
                fichier_PCM.add(ligne);
            }

        }

        Iterator<String>it_PCM=fichier_PCM.iterator();

        while(it_PCM.hasNext())
        {

            String tem_PCM = it_PCM.next();


              /*
                    *Phase de parsing
                    *
                     */
            WikipediaPageMiner miner = new WikipediaPageMiner();

            String code = miner.getPageCodeFromWikipedia(tem_PCM);


            String preprocessedCode = miner.preprocess(code);

            
            try 
            {
                 Page page = miner.parse(preprocessedCode);
                 /*
                    *Phase d'exportation vers Output vers le format.PCM
                    *
                    *
                     */
                ParserTest test_scala = new ParserTest();
                test_scala.writeToPCM(tem_PCM, page);
            }
            catch(Exception e)
            {
                ReportedCsvFile(tem_PCM,e.getMessage());
                
            }   

        }
    }


    public void ReportedCsvFile(String title, String characteristic) throws IOException
    {

        String output = System.getProperty("user.dir") + "\\Reporting\\Bugs_Reportings.csv";
        System.out.println(output);
        File csvFile = new File(output);
        if (!csvFile.exists())
        {
            BufferedWriter test = null;
            try {
                test = new BufferedWriter(new FileWriter(output));
                test.write(title+",");
                test.write(characteristic);
                test.write("\n");

               test.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else {
            PrintStream l_out = new PrintStream(new FileOutputStream(output, true));


            l_out.print(title+",");
            l_out.print(characteristic);
            l_out.print("\n");

            l_out.flush();
            l_out.close();
            l_out = null;


        }


    }
    
    
    
    
    


}