
import static org.junit.Assert.*;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.io.wikipedia.WikipediaPageMiner;
import org.diverse.pcm.io.wikipedia.export.CSVExporter;
import org.diverse.pcm.io.wikipedia.export.HTMLExporter;
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter;
import org.diverse.pcm.io.wikipedia.pcm.*;
import org.diverse.pcm.io.wikipedia.pcm.Page;
import org.diverse.pcm.io.wikipedia.ParserTest;
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


public class ParsingAutomatisationTest

{

     private String tem_PCM;
    private Page page;
    private String patht;


    private String path;
    private File Directory;
    private Calendar date;
    private String dateDuJour;
    private String sourcePath;
    List<String> fichier_PCM;
    private WikipediaPageMiner miner;
    private ParserTest test_scala;

    @Before
    public  void setUp()
    {
        path = System.getProperty("user.dir");
        sourcePath = System.getProperty("user.dir");
        date = Calendar.getInstance(); //retourne une instance de la classe calendrier
        fichier_PCM= new ArrayList<String>();
        miner = new WikipediaPageMiner();
        test_scala = new ParserTest();
    }
    @Test
    public void test() throws IOException
    {

        dateDuJour = ""; // pour contenir la date du jour
        dateDuJour += date.get(Calendar.DAY_OF_MONTH);
        dateDuJour += date.get(Calendar.MONTH)+1; //Donne un résultat entre 0 et 11
        dateDuJour += date.get(Calendar.YEAR);
          ///////FONCTION DE MINING

        int i = 0;
        String ligne = null;
        
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

            test_scala.writeToPCM(tem_PCM, page);
            System.out.println("Le Fichier " + tem_PCM + " a été parsé");

        }

        path += "\\PCM_GENERATE\\"+dateDuJour;
        sourcePath +="\\output\\model\\";
       PasteFile(sourcePath,path);
    }

    /**
     * Cette methode permet de Faire du couper-coller
     * @param sourcePath
     * @param DestinationPath
     */

    public static void PasteFile(String sourcePath,String DestinationPath)
    {
      File  Directory = new File(DestinationPath);

        if (!Directory.isDirectory()) {
            Directory.mkdirs();
        }



        String temp;
        File destination;
        File source;


        //File files = new File(sourcePath);
        // Lister les noms des contenus du répertoire courant
        String[] dir = new File(sourcePath).list();
        for (int j=0; j<dir.length; j++)
        {
            // Afficher le nom de chaque élément
//
            temp = dir[j];
            destination = new File(DestinationPath +"/"+ temp);
            source=new File(sourcePath+"/"+temp);

            try {
                if (destination.createNewFile())
                {
                    System.out.println("Le fichier a été créé");

                    boolean b = copyFile(source, destination);
                    if(b==true)
                    {
                        source.delete();
                    }
                    else
                    {

                    }
                }
                else
                    System.out.println("Erreur, Impossible de créer ce fichier");


            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }

    public static boolean copyFile(File source, File dest)
    {
        try{
            // Declaration et ouverture des flux
            java.io.FileInputStream sourceFile = new java.io.FileInputStream(source);

            try{
                java.io.FileOutputStream destinationFile = null;

                try{
                    destinationFile = new FileOutputStream(dest);

                    // Lecture par segment de 0.5Mo
                    byte buffer[] = new byte[512 * 1024];
                    int nbLecture;

                    while ((nbLecture = sourceFile.read(buffer)) != -1){
                        destinationFile.write(buffer, 0, nbLecture);
                    }
                } finally {
                    destinationFile.close();
                }
            } finally {
                sourceFile.close();
            }
        } catch (IOException e){
            e.printStackTrace();
            return false; // Erreur
        }

        return true; // Résultat OK
    }


}