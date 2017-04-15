package JSONformating;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

import org.bson.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.impl.io.KMFJSONExporter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import JSONformating.model.JSONFormat;
import JSONformating.reader.JSONReader;
import JSONformating.reader.JSONtoPCM;
import data_off.PCMUtil;

public class JSONmetamorphTest {

	static String inputpath = "../../";
	static ArrayList<PCMContainer> listPcmC1 = new ArrayList<>();
	static ArrayList<JSONFormat> listJson2 = new ArrayList<>();
	static ArrayList<PCMContainer> listPcmC3 = new ArrayList<>();
	static ArrayList<JSONFormat> listJson4 = new ArrayList<>();

	static int count = 0;

	@BeforeClass
	public static void loadPCMs() throws IOException {
		System.out.println("Begin");
		loadPCMc1();
		System.out.println("Pcm1 loaded");
		loadJSON2();
		System.out.println("Json2 loaded");
		loadPCMc3();
		System.out.println("Pcm3 loaded");
		loadJSON4();
		System.out.println("Json4 loaded");
	}

	public static void loadPCMc1() throws IOException {
		Stream<Path> paths = Files.walk(Paths.get(inputpath));

		paths.forEach(filePath -> {
			if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".pcm")) {
				count++;
				// System.err.println("New file");
				try {
					listPcmC1.add(importNewFormat(filePath.toString()));
				} catch (Exception e) {
					// System.out.println("New crash");
					// e.printStackTrace();
				}
				/*
				 * try { listPcmC.add(importOldFormat(filePath.toString())); }
				 * catch (Exception e) { //System.out.println("Old crash"); //
				 * e.printStackTrace(); }
				 */
			}
		});
		System.out.println("PCMs found : " + count);

	}

	public static void loadJSON2() throws IOException {
		for (PCMContainer pcmContainer : listPcmC1) {
			String export;
			export = exportNewFormat(pcmContainer);
			listJson2.add(JSONReader.importJSONString(export));
		}
	}

	public static void loadPCMc3() {
		for (JSONFormat jf : listJson2) {
			listPcmC3.add(JSONtoPCM.JSONFormatToPCM(jf));
		}
	}

	public static void loadJSON4() throws IOException {
		for (PCMContainer pcmContainer : listPcmC3) {
			String export;
			export = exportNewFormat(pcmContainer);
			listJson4.add(JSONReader.importJSONString(export));
		}
	}

	public static PCMContainer importOldFormat(String filename) throws IOException {
		return PCMUtil.loadPCMContainer(filename);
	}

	public static PCMContainer importNewFormat(String filename) throws IOException {
		return JSONtoPCM.JSONFormatToPCM(JSONReader.importJSON(filename));
	}

	public static String exportNewFormat(PCMContainer pcmC) throws IOException {
		return PCMtoJSON.mkNewJSONFormatFromPCM(pcmC).export();
	}

	@Test
	public void UnknowToNewparsable() {
		for (PCMContainer pcmContainer : listPcmC1) {
			String export;
			try {
				export = exportNewFormat(pcmContainer);
				new JsonParser().parse(export);
			} catch (org.bson.json.JsonParseException | IOException e) {
				assert (false);
				e.printStackTrace();
			}
		}
		assert (true);
	}

	@Test
	public void firstPCMequalsToSecondPCM() {
		if (listPcmC1.size() != listPcmC3.size()) {
			assert (false);
		}
		int i;
		for (i = 0; i < listPcmC1.size(); i++) {
			if (!listPcmC1.get(i).equals(listPcmC3.get(i))) {
				assert (false);
			}
		}
		assert (true);
	}

	@Test
	public void firstJSONequalsToSecondJSON() {
		if (listJson2.size() != listJson4.size()) {
			assert (false);
		}
		int i;
		for (i = 0; i < listJson2.size(); i++) {
			if (!listJson2.get(i).equals(listJson4.get(i))) {
				assert (false);
			}
		}
		assert (true);
	}

}
