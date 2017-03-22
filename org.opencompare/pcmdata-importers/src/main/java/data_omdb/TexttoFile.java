package data_omdb;



// io = input/output (entree/sortie)
import java.io.*;
//ce package est necessaire pour executer ce programme

//appellez bien votre fichier Test.java
public class TexttoFile
{

	//fonction main
	public static void main(String args[])
	{
		
//		String texteaecrire = "texte...\nsaut de ligne";
//		
//		Lireetecrire lee = new Lireetecrire();
//		//execution de la fonction ecrire de la classe Lireetecrire avec le String texteaecrire comme argument
//		lee.ecrire(texteaecrire);
//		//execution de la fonction lire de la classe Lireetecrire
//		lee.lire();
		
	
	}
	
	
	
	

}

class Lireetecrire
{

//fonction ecrire      void = la fonction ne retourne rien 
	//mais prend comme argument	la chaine de charactere(Sring) texte qui correspond a texteaecrire
	public void ecrire(String texte)
	{
		//on va chercher le chemin et le nom du fichier et on me tout ca dans un String
		String adressedufichier = System.getProperty("user.dir") + "\\monfichier.txt";
	
		//on me try si jamais il y a une exception
		try
		{
			
			//BufferedWriter a besoin d un FileWriter, 
			//les 2 vont ensemble, on donne comme argument le nom du fichier
			//true signifie qu on ajoute dans le fichier (append), on ne marque pas par dessus 
			FileWriter fw = new FileWriter(adressedufichier, true);
			
			// le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
			BufferedWriter output = new BufferedWriter(fw);
			
			//on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
			output.write(texte);
			//on peut utiliser plusieurs fois methode write
			
			output.flush();
			//ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
			
			output.close();
			//et on le ferme
			
		}
		catch(IOException ioe){System.out.println("erreur : " + ioe );}
		//on "catch" l exception ici si il y en a une, et on l affiche sur la console 

	}

	//je vais moins commenter cette partie c'est presque la meme chose
	public void lire()
	{
		
		try
		{
		
		
			String adressedufichier = System.getProperty("user.dir") + "\\monfichier.txt";
			
			FileReader fr = new FileReader(adressedufichier);
			BufferedReader br = new BufferedReader(fr);
			
			String texte = "";
			int a = 0;
				while(a<2) //petite boucle 2 fois
				{
					texte = texte + br.readLine() + "\n";
					a++;		
				}
			br.close();
			
			//readLine pour lire une ligne
			//note: si il n y a rien, la fonction retournera la valeur null
			
			
			System.out.println(texte);
			//on affiche le texte
		
		}
		catch(IOException ioe){System.out.println("erreur : " + ioe);}
		
		
	
	}

}
