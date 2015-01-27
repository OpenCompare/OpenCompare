import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.WikipediaPageMiner;
import org.diverse.pcm.io.wikipedia.export.CSVExporter;
import org.diverse.pcm.io.wikipedia.export.HTMLExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.diverse.pcm.io.wikipedia.pcm.Matrix;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import static org.junit.Assert.*;
import org.junit.Test;
import static scala.collection.JavaConversions.*;

import org.diverse.pcm.io.wikipedia.ParserTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Cette classe permet de générer les WikiText comme on le faisait avec WikipediaGeneratePCM pour le sprint1
 */
public class WikiTextGenerateTest

{

    @Test



    public void test()
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

                System.out.println(code);
                System.out.println(tem_PCM);
                String preprocessedCode = miner.preprocess(code);

                System.out.println(preprocessedCode);

                Page page = miner.parse(preprocessedCode);



                    /*
                    *Phase d'exportation vers Output vers le format.PCM
                    *
                    *
                     */
                ParserTest test_scala=new ParserTest();
                test_scala.writeToWikiText(tem_PCM,page);


            }
        }


}