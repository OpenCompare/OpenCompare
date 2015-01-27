import org.junit.After;
import org.junit.Before;

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

public class WikipediaExport

{

    String path_Recovery_File;
    List<String> fichier_PCM;
    WikipediaPageMiner miner;
    String path;
    
    
    @Before
    public void setup()
    {
        miner= new WikipediaPageMiner();
        
    }
    @Test
    public void test()
    {

 /**Phase de recuperation de tous les fichiers à tester dans la list_PCMs.text
  * Cette Classe crée le repertoire Code_ PCM lequel permet de ranger les fichiers.txt
 */

        String path_Recovery_File;
        int i = 0;
        String ligne = null;
       
        fichier_PCM=new ArrayList<String>();
        String[]tab_PCM=null;


        path = System.getProperty("user.dir");
        path += "\\resources\\list_of_PCMs.txt";


        BufferedReader lire_Fichier = null;
        try {
            lire_Fichier = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (lire_Fichier == null) try {
            throw new FileNotFoundException("Fichier non trouvé: " + path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        else
        {


            try {
                while ((ligne=lire_Fichier.readLine())!=null)
                {
                    i++;
                    fichier_PCM.add(ligne);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Iterator<String> it_PCM=fichier_PCM.iterator();
        while(it_PCM.hasNext())
        {
            String tem_PCM = it_PCM.next();

              /*
               *Phase de parsing
                 *
                 */

            String code = miner.getPageCodeFromWikipedia(tem_PCM);
            path_Recovery_File=System.getProperty("user.dir") + "\\Code_PCM";

            /**
             * créer le repertoire adéquat qui va comprendre tous les fichiers code_PCM
             */
            File Directory = new File(path_Recovery_File);

            if (!Directory.isDirectory())
            {
                Directory.mkdir();
            }


            FileWriter writer = null;

            try
            {

                writer = new FileWriter(path_Recovery_File + "//" + tem_PCM + ".txt", true);
                BufferedWriter output = new BufferedWriter(writer);
                String preprocessedCode = miner.preprocess(code);
                output.write(code);
                output.flush();
                output.close();

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

        }
    }








}
