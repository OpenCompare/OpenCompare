package pcm_Filter.simple;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Stream;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;

import org.opencompare.api.java.io.PCMLoader;

import pcm_InfoContainer.*;
import pcm_Filter.simple.pcm_predicate.*;




public class Main {

	public static final boolean writefile = false;

	public static final String inputpath = "input-pcm/oldformat";
	public static final String outputpath = "../../output-model/";

	// "input-pcm" testing inputs
	// "../../input-model" prod inputs
	// "D:/Windows/input-pcm"

	// "output-pcm/" //test outputs
	// "../../output-model/" //prod outputs
	// "output-pcm-JS/" //PCM prod for JS Team

	public static void main(String[] args) throws IOException {

		long startTime = System.nanoTime();

		Stream<Path> paths = Files.walk(Paths.get(inputpath));

		paths.forEach(filePath -> {
			if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".pcm")) {
				System.out.println("> PCM imported from " + filePath);

				File pcmFile = new File(filePath.toString());

				PCMLoader loader = new KMFJSONLoader();
				List<PCMContainer> pcmContainers = null;
				try {
					pcmContainers = loader.load(pcmFile);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (PCMContainer pcmContainer : pcmContainers) {
					// Get the PCM
					
					PCMInfoContainer pcmic = new PCMInfoContainer(pcmContainer);

					// for using multiple filters
					PCMCompositeFilter pFilter = new PCMCompositeFilter();
					pFilter.addFilter(new PCMPredicateMinRowProduct());
					pFilter.addFilter(new PCMPredicateMinColumnProduct());

					Boolean isNicePCM = pFilter.isSatisfiable(pcmic);

					if (writefile) {
						// now determine if the pcm is good
						if (isNicePCM) {
							KMFJSONExporter pcmExporter = new KMFJSONExporter();
							String pcmString = pcmExporter.export(pcmContainer);

							Path p = Paths.get(outputpath + filePath.getFileName());
							try {
								Files.write(p, pcmString.getBytes());
							} catch (Exception e) {
								e.printStackTrace();
							}
							System.out.println("> PCM exported to " + p);
						}
					}
				}
			}
		});

		paths.close();

		long endTime = System.nanoTime();
		System.out.println("Took " + (endTime - startTime) / (1000000) + " ms");
		System.out.println("Took " + (endTime - startTime) / (1000000000) + " s");

	}

}