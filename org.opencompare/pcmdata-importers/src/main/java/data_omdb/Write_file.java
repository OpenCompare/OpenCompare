package data_omdb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
/**
 * Rediriger les entr�es de la console vers un fichier
 */
public class Write_file {
 
    public static void main(String[] args) {
    }
    
    public static void write_file(String f){
    	 //Cr�er un nom de fichier bas� sur la date et l'heure
        String filename=f;
        //ouvrir le fichier
        File file = new File("output/"+filename);
        try {
            PrintStream printStream = new PrintStream(file);
            System.setOut(printStream);
//            System.out.println("Exemple de log de l'application JAVA");
//            System.out.println("Tout ce qui apparait dans la console est repris dans le fichier");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
}
