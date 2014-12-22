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
import org.junit.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import java.io.*;

import static org.junit.Assert.*;

/**
 * Cette classe permet d'executer et de generér les.PCM non pas à partir de Wikipedia mais en local
 * Les Fichiers se trouvent dans le Repertoire de Code_PCM
 * C'est à partir de ces fichiers qu'on fait manuellement les jeux de test pour tester la robustesse du parser
 *
 */
public class WikipediaRobustCode


{
    @Test
    public void Test() {


        WikipediaPageMiner miner = new WikipediaPageMiner();
        Page page = null;
        String[] list_file = null;
        String ligne_file = null;
        String Edit_Code = null;
        String prepossed_code = null;
        StringBuffer edit;

        String link = System.getProperty("user.dir") + "\\Code_PCM";
        File repertoire = new File(link);


        if (repertoire.isDirectory()) {

            list_file = repertoire.list();;
        }

        /**
         * Parcours du Repertoire
         *Trouver les .text et parsez-les
         */
        for (int i = 0; i < list_file.length; i++) {
            String temp_file = list_file[i];

            System.out.println(temp_file);
            BufferedReader lire_code_PCM = null;
            try {
                lire_code_PCM = new BufferedReader(new FileReader(link + "\\" + temp_file));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            if (temp_file.endsWith(".txt") == true)
                try {
                    while ((ligne_file = lire_code_PCM.readLine()) != null)

                    {

                        Edit_Code +=ligne_file+"\n";
                    }

                    StringBuffer new_Title=new StringBuffer(temp_file);
                    new_Title.delete(new_Title.indexOf(".txt"),temp_file.length());


                    edit = new StringBuffer(Edit_Code);
                    edit.delete(0,4);

                    page = miner.parse(edit.toString());

                    ParserTest parser_PCM = new ParserTest();
                    parser_PCM.writeToPCM(new_Title.toString(), page);


                } catch (IOException e) {
                    e.printStackTrace();
                }


        }

    }
}