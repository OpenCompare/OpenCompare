package data_omdb;
import java.io.IOException;
import java.io.* ;
import java.net.URL ;
import java.nio.charset.StandardCharsets ;
import org.json.* ;

public class Recuperation_Omdb {
		
	public static void main(String[] args) throws IOException, JSONException {
		String nom_id = "" ;
		
		for(int i=1480055;i<1480056;i++)
		{
			long j = 0 ;
			j = j+i ;
			nom_id = String.valueOf(j) ;
			int taille = nom_id.length() ;
			while(taille <7){
				nom_id = "0" + nom_id ;
				taille ++ ;
			}
			nom_id = "tt"+nom_id ;
			System.out.println(nom_id);
			URL url = new URL("http://www.omdbapi.com/?i="+ nom_id +"&plot=short&r=json") ;
			BufferedReader in = new BufferedReader (new InputStreamReader(url.openStream(),StandardCharsets.UTF_8)) ;
			String input = in.readLine() ;
			in.close(); 
			
			JSONObject obj = new JSONObject(input) ;
			
			System.out.println(input);
			System.out.println(obj.getString("Response"));
			
			//on regarde si le film existe
			if(!(obj.getString("Response").equals("False" ))){
				//TODO il faut r�cuperer tous les films pour les mettre en pcm
				
				System.out.println("le film est "+obj.getString("Title"));
			}
			else{
				System.out.println("Aucun film pour cet id : " + nom_id);
			}
		}
			
	}
	
	
}
