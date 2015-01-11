import static org.junit.Assert.*;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.WikipediaPageMiner;
import org.diverse.pcm.io.wikipedia.export.CSVExporter;
import org.diverse.pcm.io.wikipedia.export.HTMLExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.pcm.*;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import org.diverse.pcm.io.wikipedia.ParserTest;
import org.diverse.pcm.io.wikipedia.export.WikiTextExporter;
import org.junit.Before;
import org.junit.Test;
import scala.reflect.internal.AnnotationInfos;

import java.io.*;
import java.io.File;
import java.util.Calendar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.assertNotNull;

import static scala.collection.JavaConversions.*;


/**
 *
 * Cette Classe permet de créer des .pcm temporaires dans PCM0 dans le but de les comparer avec .pcm de PCM1
 */

public class WikipediaPCMo extends ParsingAutomatisationTest


{

    private String tem_PCM;
    private Page page;

    private WikipediaPageMiner miner;
    private  PCMModelExporter pcmExporter;
    private  ParserTest test_scala;


    @Before
    public void setUp()
    {
        miner = new WikipediaPageMiner();
        pcmExporter = new PCMModelExporter();
        test_scala=new ParserTest();
    }

    
    @Test
    public void test()
    {


        miner = new WikipediaPageMiner();
        
            ///////FONCTION DE MINING

            int i = 0;
            String ligne = null;
            List<String> fichier_PCM = new ArrayList<String>();
            String[] tab_PCM = null;

            String path_res = System.getProperty("user.dir");

            path_res += "\\resources\\list_of_PCMs.txt";
            BufferedReader lire_Fichier = null;
            try {
                lire_Fichier = new BufferedReader(new FileReader(path_res));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (lire_Fichier == null) try {
                throw new FileNotFoundException("Fichier non trouvé: " + path_res);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            else {


                try {
                    while ((ligne = lire_Fichier.readLine()) != null) {
                        i++;


                        // System.out.println(line);
                        fichier_PCM.add(ligne);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        Iterator<String> it_PCM = fichier_PCM.iterator();
        while (it_PCM.hasNext())
        {
            tem_PCM = it_PCM.next();



            //Parse article from Wikipedia
            String code = miner.getPageCodeFromWikipedia(tem_PCM);
            String preprocessedCode = miner.preprocess(code);
            page = miner.parse(preprocessedCode);


            // PCM model export
             pcmExporter = new PCMModelExporter();
            List<PCM> pcms = seqAsJavaList(pcmExporter.export(page));
            //assertFalse(pcms.isEmpty());
            
            ParserTest test_scala=new ParserTest();
            test_scala.writeToPCM(tem_PCM, page);
            test_scala.writeToWikiText(tem_PCM, page);
        }
            /**
             * Faire le Couper-Coller
             */
        String Destination_path=System.getProperty("user.dir")+"\\PCM0";
        String Source_Path=System.getProperty("user.dir")+"\\output\\model";
        PasteFile(Source_Path,Destination_path);


    }
}
