package pcm_Metric;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

//import org.junit.Test;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import com.opencsv.CSVWriter;

import pcm_InfoContainer.*;

public class PCMMetricMain {

	public static final String inputpath = "input-pcm/oldformat";
	public static final String outputpath = "input-pcm/oldformat";

	// "../../input-model"
	// "input-pcm"
	// @Test
	public static void main(String[] args) throws IOException {

		Stream<Path> paths = Files.walk(Paths.get(inputpath));

		File outputcsv = new File("./metrics/metrictest.csv");

		try {
			outputcsv.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try (Writer writer = new BufferedWriter(new FileWriter(outputcsv))) {
			CSVWriter csvwriter = new CSVWriter(writer, ';', '\"');
			String[] header = { "Name", "Feature ", "Products ", "Cells", "Empty Cells", "Ratio EmCell",
					"Nombre de Features Homogenes", "Ratio de Features Homogenes",
					"Nombre de Features Homogenes Numeriques", "Product Chartable", "Testing Score" };
			csvwriter.writeNext(header);// writing the header

			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".pcm")) {
					System.out.println("> PCM read from " + filePath);

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
						PCMInfoContainer pcmic = null;

						try {
							pcmic = new PCMInfoContainer(pcmContainer);
						} catch (Exception e) {
						}

						if (pcmic != null) {
							String[] str = { filePath.getFileName().toString(),
									pcmic.getStatPcm().getNbFeatures().toString(),
									pcmic.getStatPcm().getNbProducts().toString(),
									pcmic.getStatPcm().getNbCells().toString(),
									pcmic.getStatPcm().getNbEmptyCells().toString(),
									pcmic.getStatPcm().getRatioEmptyCells().toString(),
									pcmic.getStatPcm().getNbFeaturesHomog().toString(),
									pcmic.getStatPcm().getRatioFeaturesHomog().toString(),
									pcmic.getStatPcm().getNbFeaturesHomogNumeric().toString(),
									pcmic.isProductChartable().toString(), };

							csvwriter.writeNext(str);

							if (pcmic.isProductChartable()) {
								KMFJSONExporter pcmExporter = new KMFJSONExporter();
								String pcmString = pcmExporter.export(pcmContainer);

								Path p = Paths.get(outputpath + "/" + filePath.getFileName());
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

			csvwriter.close();
		} catch (

		IOException e) {
			e.printStackTrace();
		}

		paths.close();

	}
}
