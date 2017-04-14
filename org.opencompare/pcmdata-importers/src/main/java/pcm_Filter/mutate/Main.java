package pcm_Filter.mutate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import data_off.PCMUtil;
import pcm_InfoContainer.PCMInfoContainer;
import JSONformating.model.* ;
import JSONformating.reader.*;


public class Main {

	public static String inputpath = ""; // give path with argv
	public static String outputpath = ""; // give path with argv

	public static void main(String[] args) throws IOException {

		
		// TODO : le pcm mutate est bien différent du pcm de base, mais les changements ne sont pas pris en compte. A travailler !
		
		// inputpath = args[0];
		inputpath = "off_output/pcms/test_paget/input";
		outputpath = "off_output/pcms/test_paget/output";

		try {


			Stream<Path> paths = Files.walk(Paths.get(inputpath));

			paths.forEach(filePath -> {
				
				if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".pcm")) {
					System.out.println("> PCM read from " + filePath);
				     
					//File pcmFile = new File(filePath.toString());

					PCMContainer pcmC = null ;
					//PCMContainer pcmC = JSONtoPCM.JSONFormatToPCM(JSONReader.importJSON(filePath.toString())) ;
					//PCMLoader loader = new KMFJSONLoader();
					//List<PCMContainer> pcmContainers = null;
					try {
						pcmC = JSONtoPCM.JSONFormatToPCM(JSONReader.importJSON(filePath.toString())) ;
					} catch (Exception e) {
						e.printStackTrace();
					}

//					for (PCMContainer pcmContainer : pcmContainers) {
//						// Get the PCM
						
						PCMInfoContainerMuted pcmic = null;
						
						try {
							System.out.println(pcmic.toString());
							pcmic = new PCMInfoContainerMuted(pcmC);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (pcmic != null) {
							if (pcmic.isSameSizePcm()) {
								System.out.println("> PCM muted is the same");
							} else {
								pcmC.setPcm(pcmic.getMutedPcm().getPcm());
								KMFJSONExporter pcmExporter = new KMFJSONExporter();
								String pcmString = pcmExporter.export(pcmC);
								
								Path p = Paths.get(outputpath + "muted_" ) ;//filePath.getFileName());
								try {
									Files.write(p, pcmString.getBytes());
								} catch (Exception e) {
									e.printStackTrace();
								}
								System.out.println("> PCM exported to " + p);
							}
						}
						else {
							System.out.println("> PCM corrompu");
						}

				}

			});
			// mongoClient.close();
			paths.close();

		} catch (Exception e) {
			// mongoClient.close();
			e.printStackTrace();
		}
	}
}
