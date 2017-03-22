package data_omdb;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OMDBProduct {
	
	public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	// all parameters/properties 
	private String Title ;
	private String Year ;
	private String Rated  ;
	private String Released ;
	private String Runtime  ; 	// TODO: what about duration? is it runtime? 
	private String[] Genre ;
	private String Director  ;
	private String Writer ;
	private String[] Actors ;
	private String Plot ;
	private String[] Language ;
	private String[] Country ;
	private String Poster ;
	private String Metascore ;
	private String imdbRating ; // format 7.7
	private String imdbVotes ; // format 802,661
	private String imdbID ;
	private String Type ;
	private String totalSeasons ;
	private String seriesID ;
	private String Season;
	private String Episode;
	
	public OMDBProduct(){
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getRated() {
		return Rated;
	}

	public void setRated(String rated) {
		Rated = rated;
	}

	public String getReleased() {
		return Released;
	}

	public void setReleased(String released) {
		Released = released;
	}

	public String getRuntime() {
		return Runtime;
	}

	public void setRuntime(String runtime) {
		Runtime = runtime;
	}

	public String[] getGenre() {
		return Genre;
	}
	
	public String getGenreString() throws IOException{
		return OBJECT_MAPPER.writeValueAsString(Genre);
		
	}

	public void setGenre(String[] genre) {
		Genre = genre;
	}
	
	public void setGenreFromString(String Genre) {
			if(Genre.isEmpty()){
				this.Genre = null;
			}
			else{
				this.Genre = Genre.split(",");
			}
		
	}

	public String getDirector() {
		return Director;
	}

	public void setDirector(String director) {
		Director = director;
	}

	public String getWriter() {
		return Writer;
	}

	public void setWriter(String writer) {
		Writer = writer;
	}

	public String[] getActors() {
		return Actors;
	}
	
	public String getActorsString() throws IOException{
		return OBJECT_MAPPER.writeValueAsString(Actors);
		
	}

	public void setActors(String[] actors) {
		Actors = actors;
	}
	
	public void setActorsFromString(String Actors) {
		if(Actors.isEmpty()){
			this.Actors = null;
		}
		else{
			this.Actors = Actors.split(",");
		}
	
	}

	public String getPlot() {
		return Plot;
	}

	public void setPlot(String plot) {
		Plot = plot;
	}

	public String[] getLanguage() {
		return Language;
	}
	
	public String getLanguageString() throws IOException{
		return OBJECT_MAPPER.writeValueAsString(Language);
		
	}

	public void setLanguage(String[] language) {
		Language = language;
	}
	
	public void setLanguageFromString(String Language) {
		if(Language.isEmpty()){
			this.Language = null;
		}
		else{
			this.Language = Language.split(",");
		}
	
	}

	public String[] getCountry() {
		return Country;
	}

	public String getCountryString() throws IOException{
		return OBJECT_MAPPER.writeValueAsString(Country);
		
	}
	
	public void setCountry(String[] country) {
		Country = country;
	}

	public void setCountryFromString(String Country) {
		if(Country.isEmpty()){
			this.Country = null;
		}
		else{
			this.Country = Country.split(",");
		}
	
	}
	
	public String getPoster() {
		return Poster;
	}

	public void setPoster(String poster) {
		Poster = poster;
	}

	public String getMetascore() {
		return Metascore;
	}

	public void setMetascore(String metascore) {
		Metascore = metascore;
	}

	public String getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}

	public String getImdbVotes() {
		return imdbVotes;
	}

	public void setImdbVotes(String imdbVotes) {
		this.imdbVotes = imdbVotes;
	}

	public String getImdbID() {
		return imdbID;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getTotalSeasons() {
		return totalSeasons;
	}

	public void setTotalSeasons(String totalSeasons) {
		this.totalSeasons = totalSeasons;
	}

	public String getSeriesID() {
		return seriesID;
	}

	public void setSeriesID(String seriesID) {
		this.seriesID = seriesID;
	}

	public String getSeason() {
		return Season;
	}

	public void setSeason(String season) {
		Season = season;
	}

	public String getEpisode() {
		return Episode;
	}

	public void setEpisode(String episode) {
		Episode = episode;
	}
}
