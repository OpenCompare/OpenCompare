package data_omdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL ;
import java.nio.charset.StandardCharsets ;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * Import from OMDB REST APIs (JSON format) a "product representation" 
 * that is then transformed into a CSV
 *
 */
public class OMDBToProduct {
	
	private static final Logger _log = Logger.getLogger(OMDBToProduct.class.getName());
	
		
	/*
	 * TODO
	 * Could be parameters of procedures
	 */
	public static final int STARTING_OMDB_ID = 944000;
	public static final int NUMBER_OF_OMDB_PRODUCTS = 500; 
	
	private ConformanceProduct conformanceProduct = null;
	
	public OMDBToProduct(ConformanceProduct conformanceProduct) {
		this.conformanceProduct = conformanceProduct;
	}
	
	public OMDBToProduct() {
		this.conformanceProduct = new ConformanceProductOK(); // always true, no constraints 
	}

	
	public OMDBProduct createProductFromJson(JSONObject obj) throws JSONException  {
		
		List<String> vide = new ArrayList<String>(); // FIXME very dangerous to reuse the same reference
		vide.add("");
		

		//on regarde si le film existe
		if(obj.getString("Response").equals("False")){
			// qunad le film n'existe pas
			return null ;
		}
		else {
			OMDBProduct pro = new OMDBProduct() ;			
			
		
			try {
			pro.setImdbID(obj.getString("imdbID"));
			pro.setTitle(obj.getString("Title"));
			pro.setYear(obj.getString("Year"));
			pro.setRated(obj.getString("Rated"));
			
			pro.setPlot(obj.getString("Plot"));
			if(!pro.getPlot().equals("N/A")) {
				pro.setPlot("http://www.imdb.com/title/"+ pro.getImdbID() + "/plotsummary?ref_=tt_ov_pl");
			}
			pro.setReleased(obj.getString("Released"));
			pro.setRuntime(obj.getString("Runtime"));
			pro.setGenreFromString(obj.getString("Genre"));
			pro.setDirector(obj.getString("Director")) ;
			pro.setWriter(obj.getString("Writer"));
			pro.setActorsFromString(obj.getString("Actors"));
			pro.setLanguageFromString(obj.getString("Language"));
			pro.setCountryFromString(obj.getString("Country"));
			pro.setPoster(obj.getString("Poster"));
			pro.setMetascore(obj.getString("Metascore"));
			pro.setImdbRating(obj.getString("imdbRating"));
			pro.setImdbVotes(obj.getString("imdbVotes").replaceAll(",", ""));
			pro.setType(obj.getString("Type"));
			
			pro.setTotalSeasons(obj.getString("totalSeasons"));
			pro.setSeriesID(obj.getString("seriesID"));
			pro.setSeason(obj.getString("Season"));
			pro.setEpisode(obj.getString("Episode"));
		
			}
			catch (JSONException e) {
				_log.warning("JSON exception OMDB " + e.getMessage());
			}
			
			/*
			if (pro.getPoster() == null) {
				return null; 
			}*/
			if (!conformanceProduct.isOK(pro)) 
				return null;
			
			return pro ;
			
		}
		
	}

	
	public static JSONObject idToJson(long id) throws IOException, JSONException{
		String nom_id = "" ;
		long j = 0 ;
		j = j+id ;
		nom_id = String.valueOf(j) ;
		int taille = nom_id.length() ;
		while(taille <7){
			nom_id = "0" + nom_id ;
			taille ++ ;
		}
		nom_id = "tt"+nom_id ;
		URL url = new URL("http://www.omdbapi.com/?i="+ nom_id +"&plot=short&r=json") ;
		BufferedReader in = new BufferedReader (new InputStreamReader(url.openStream(),StandardCharsets.UTF_8)) ;
		String input = in.readLine() ;
		in.close(); 
		
		JSONObject obj = new JSONObject(input) ;
		return obj ;
	}


	
	/**
	 * @param t OMDB entries can be movies, series, episodes, etc. 
	 * @return a CSV representation of all OMDB entries that are of type specified by t
	 * @throws JSONException
	 * @throws IOException
	 */
	public String mkCSV(OMDBMediaType t) throws JSONException, IOException {
		Collection<OMDBMediaType> omTypes = new ArrayList<OMDBMediaType>();
		omTypes.add(t);
		Map<OMDBMediaType, String> csvs = mkCSVs(omTypes);
		if (!csvs.containsKey(t))
			return "";
		return csvs.get(t);
	}
	
	
	/**
	 * TODO generalize and design a "filter" for retaining only some OMDB entries
	 * @param omTypes OMDB entries can be movies, series, episodes, etc.
	 * @return a CSV representation of all OMDB entries that are of one of the type specified by omTypes 
	 * @throws JSONException
	 * @throws IOException
	 */
	public Map<OMDBMediaType, String> mkCSVs(Collection<OMDBMediaType> omTypes) throws JSONException, IOException {
		
		
		Map<OMDBMediaType, String> omdbTypes2CSV = new HashMap<OMDBMediaType, String>();
	
		for(int i = STARTING_OMDB_ID ; i < (STARTING_OMDB_ID + NUMBER_OF_OMDB_PRODUCTS); i++)	{
					
			 OMDBProduct p  = createProductFromJson(idToJson(i)) ;
			 			 
			 
			 if (p != null)
			 {
				 // seeking if it is a movie, an episode, etc.
				 String oType = p.getType();
				 boolean found = false;
				 for (OMDBMediaType omType : omTypes) {					
					 if(oType.equals(omType.toString())) { 
						 found = true;
						 String pdt = omdbTypes2CSV.get(omType);
						 if (pdt == null)
							 omdbTypes2CSV.put(omType, "");
						 else {							 
							 pdt += OMDBCSVProductFactory.getInstance().mkCSVProduct(p, omType) + System.getProperty("line.separator");
							 omdbTypes2CSV.put(omType, pdt);
						 }
					 }					 
				}
				//if(!found) 
					// _log.warning("Unknown type " + oType);  
						 
			 }
			 
			 else {
				 _log.warning("Unable to load OMDB entry " + i);  
			 }
		}
				
		return omdbTypes2CSV;		
	}
	



	
}

