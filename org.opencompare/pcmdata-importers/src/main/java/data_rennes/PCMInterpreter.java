package data_rennes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.extractor.CellContentInterpreter;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.io.CSVLoader;
import org.opencompare.api.java.io.PCMDirection;


public class PCMInterpreter {

	public static void main(String[] arg0) throws IOException {
		// get all available datasets
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL("https://data.rennesmetropole.fr/api/datasets/1.0/search/?rows=200");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
			e.printStackTrace();
        }
        // parse JSON response
        JSONObject resp;
		try {
			resp = (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();
	        JSONArray datasets = resp.getJSONArray("datasets");
	        for (int i=0; i<datasets.length(); i++) {
	        	String dsid = datasets.getJSONObject(i).getString("datasetid");
	        	if (i > 173 && datasets.getJSONObject(i).getJSONObject("metas").getInt("records_count") > 0) {
	        		System.out.println("downloading "+i+"/"+datasets.length()+" "+dsid+"");
	        		PCMContainer pcm = downloadDatasetAsPCM(dsid);
	        		if (pcm != null) writeToFile(pcm, "output-pcm/rennes/"+dsid+".pcm");
	        	}
	        }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static PCMContainer downloadDatasetAsPCM(String dsid) {
		// download dataset
		StringBuilder csv = new StringBuilder();
        try {
            URL url = new URL("https://data.rennesmetropole.fr/api/records/1.0/download/?dataset="+dsid);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    csv.append(line).append("\n");
                }
                bufferedReader.close();
            } catch (IOException e) {
            	System.out.println("forbidden");
            } finally {
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
			e.printStackTrace();
        }
        // convert to PCM
		CSVLoader csvL = new CSVLoader(new PCMFactoryImpl(), new CellContentInterpreter(new PCMFactoryImpl()), ';', '"',
				PCMDirection.PRODUCTS_AS_LINES);
		try {
			List<PCMContainer> pcmC = csvL.load(csv.toString());
			PCMContainer pcmContainer = pcmC.get(0);
			return pcmContainer;
		} catch(OutOfMemoryError e) {
			System.out.println(dsid+" failed (oom)");
			return null;
		}
	}

	private static void writeToFile(PCMContainer pcmContainer, String path) throws IOException {
		KMFJSONExporter exporter = new KMFJSONExporter();
		String json = null;
		try {
			json = exporter.export(pcmContainer);
		} catch (OutOfMemoryError e) {
			System.out.println("oom'd");
			return;
		}
		// Write modified PCM
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
		writer.write(json);
		writer.close();
	}

}
