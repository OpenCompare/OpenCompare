package data_omdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.opencsv.CSVWriter;


public class OMDBCSVProductFactory {
	
	private static OMDBCSVProductFactory _instance = null;
	
	private OMDBCSVProductFactory() {
		
	}
	
	public static OMDBCSVProductFactory getInstance() {
		if (_instance == null)
			_instance = new OMDBCSVProductFactory();
		return _instance;
	}
	
	
	/**
	 * A CSV representation of "Product" (in fact a CSV line)
	 * the CSV representation depends on OMDB type  
	 * @param p
	 * @param t
	 * @return
	 */
	public String mkCSVProduct(OMDBProduct p, OMDBMediaType t) {
		
		
		
		if (t.equals(OMDBMediaType.MOVIE))
			return mkCSVProductMovie(p) ;
		if (t.equals(OMDBMediaType.SERIES))
			return mkCSVProductSerie(p) ;
		if (t.equals(OMDBMediaType.EPISODE))
			return mkCSVProductEpisode(p);
		
		return null;
	}
	
	public String mkCSVProductMovie(OMDBProduct p) {
		
		//TODO OFFactsCSVCreate 
		/*CSVWriter csvwriter = new CSVWriter(writer, ';', '"');
		String[] header  = {"id","product_name","countries","ingredients","brands","stores","image_url"};
		csvwriter.writeNext(header);//writing the header
		Document product;
		int count = 0;
		while(cursor.hasNext()){
			product = cursor.next();
			csvwriter.writeNext(OFFToProduct.mkOFFProductStrings(OFFToProduct.mkOFFProductFromBSON(product)));
			count++;*/
		
		
		
		
 		 String out = "" ;
 		 
 		 
		 out  += "\""+ p.getImdbID() + "\";\"";
		 out  += p.getTitle() + "\";\"" ;
		 out  += p.getImdbRating() + "\";\"";
		 out  += p.getRuntime() + "\";\"";
		 out  += p.getImdbVotes() + "\";\"";
		 int cpt = 0 ;
		 //affichage du genre du film parcours de la liste
		 for(String g : p.getGenre()){
			 if(cpt == 0){
				out  +=  g ;
				 cpt++ ;
			 }
			 else{
				 out  += "," + g;
			 }
			 	
		 }
		 out  += "\";\"";
		 
		 out  += p.getDirector() + "\";\"";
		 out  += p.getWriter() + "\";\"";
		 cpt = 0 ;
		 for(String a : p.getActors()){
			 if(cpt == 0){
				 out  += a;
				 cpt ++ ;
			 }
			 else{
				 out  += "," + a;
			 }
		 }
		 out  += "\";\"";
		 cpt = 0 ;
		 for(String b : p.getLanguage()){
			 if(cpt == 0){
				 out  += b;
				 cpt ++ ;
			 }
			 else{
				 out  += "," + b;
			 }
		 }
		 out  += "\";\"";cpt = 0 ;
		 for(String c : p.getCountry()){
			 if(cpt == 0){
				 out  += c;
				 cpt ++ ;
			 }
			 else{
				 out  += "," + c;
			 }
		 }
		 out  += "\";\"";
		 out  += p.getYear() + "\";\"";
		 out  += p.getMetascore() + "\";\"";
		 out  += p.getPoster() + "\";\"";
		 out  += p.getPlot() + "\";";
		 
		 

		 return out ;
	}
	
	public String mkCSVProductSerie (OMDBProduct p) {

		 String out = "" ;
		 out  += "\""+ p.getImdbID() + "\";\"";
		 out  += p.getTitle() + "\";\"" ;
		 out  += p.getTotalSeasons() +"\";\"";
		 out  += p.getImdbRating() + "\";\"";
		 out  += p.getImdbVotes() + "\";\"";
		 int cpt = 0 ;
		 //affichage du genre du film parcours de la liste
		 for(String g : p.getGenre()){
			 if(cpt == 0){
				out  +=  g ;
				 cpt++ ;
			 }
			 else{
				 out  += "," + g;
			 }
			 	
		 }
		 out  += "\";\"";
		 
		 out  += p.getDirector() + "\";\"";
		 out  += p.getWriter() + "\";\"";
		 cpt = 0 ;
		 for(String a : p.getActors()){
			 if(cpt == 0){
				 out  += a;
				 cpt ++ ;
			 }
			 else{
				 out  += "," + a;
			 }
		 }
		 out  += "\";\"";
		 cpt = 0 ;
		 for(String b : p.getLanguage()){
			 if(cpt == 0){
				 out  += b;
				 cpt ++ ;
			 }
			 else{
				 out  += "," + b;
			 }
		 }
		 out  += "\";\"";cpt = 0 ;
		 for(String c : p.getCountry()){
			 if(cpt == 0){
				 out  += c;
				 cpt ++ ;
			 }
			 else{
				 out  += "," + c;
			 }
		 }
		 out  += "\";\"";
		 out  += p.getYear() + "\";\"";
		 out  += p.getMetascore() + "\";\"";
		 out  += p.getPoster() + "\";\"";
		 out  += p.getPlot() + "\";";
		 
			 
		 return out ;
	}
	
	public String mkCSVProductEpisode(OMDBProduct p){

		String out = "";
		 out  += "\""+ p.getImdbID() + "\";\"";
		 out  += p.getTitle() + "\";\"" ;
		 out  += p.getSeriesID() + "\";\"";
		 out  += p.getSeason() + "\";\"";
		 out  += p.getEpisode() + "\";\"";
		 out  += p.getRuntime() + "\";\"";
		 out  += p.getImdbRating() + "\";\"";
		 out  += p.getImdbVotes() + "\";\"";
		 int cpt = 0 ;
		 //affichage du genre du film parcours de la liste
		 for(String g : p.getGenre()){
			 if(cpt == 0){
				out  +=  g ;
				 cpt++ ;
			 }
			 else{
				 out  += "," + g;
			 }
			 	
		 }
		 out  += "\";\"";
		 
		 out  += p.getDirector() + "\";\"";
		 out  += p.getWriter() + "\";\"";
		 cpt = 0 ;
		 for(String a : p.getActors()){
			 if(cpt == 0){
				 out  += a;
				 cpt ++ ;
			 }
			 else{
				 out  += "," + a;
			 }
		 }
		 out  += "\";\"";
		 cpt = 0 ;
		 for(String b : p.getLanguage()){
			 if(cpt == 0){
				 out  += b;
				 cpt ++ ;
			 }
			 else{
				 out  += "," + b;
			 }
		 }
		 out  += "\";\"";cpt = 0 ;
		 for(String c : p.getCountry()){
			 if(cpt == 0){
				 out  += c;
				 cpt ++ ;
			 }
			 else{
				 out  += "," + c;
			 }
		 }
		 out  += "\";\"";
		 out  += p.getYear() + "\";\"";
		 out  += p.getMetascore() + "\";\"";
		 out  += p.getPoster() + "\";\"";
		 out  += p.getPlot() + "\";";
		 
		 return out ;
	}

	public String mkHeaders(OMDBMediaType t) {
		String str = "";
		List<String> headers = new ArrayList<String>();
		if (t.equals(OMDBMediaType.MOVIE))
				headers = Arrays.asList("imdbID", "title", "imdbRating", "runtime", "imdbVotes", "genre", "director", 
		 "writer", "actors", "language", "country", "year", "metascore", "poster", "plot");
		else if (t.equals(OMDBMediaType.SERIES)) {
			headers = Arrays.asList("imdbID", "title", "totalSeasons", "imdbRating", "imdbVotes", "genre", "director", 
					 "writer", "actors", "language", "country", "year", "metascore", "poster", "plot");
		}
		else { // EPISODES
			headers = Arrays.asList("imdbID", "title", "seriesID", "season", "episode","runtime", "imdbRating", "imdbVotes", "genre", "director", 
					 "writer", "actors", "language", "country", "year", "metascore", "poster", "plot");
		}
		
		for (String h : headers) {
			str += h + ";";
		}
		return str;
		
	}

}
