package pcm_Filter.mutate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;

import org.opencompare.api.java.io.PCMLoader;

import pcm_Filter.PCMInfoContainer;


public class MainMutate {

	/*
	 * https://github.com/FAMILIAR-project/productcharts/blob/master/src/main/java/org/opencompare/PCMHelper.java
	 * https://github.com/OpenCompare/wikipedia-dump-analysis/blob/master/src/main/scala/org/opencompare/analysis/analyzer/ValueAnalyzer.scala
	 */
	
	
	public static final double ratio_vide = 0.5 ;
	
	public static final boolean writefile = false;

	public static final String pcm_path = "";

	// "input-pcm" testing inputs
	// "../../input-model" prod inputs
	// "D:/Windows/input-pcm"

	// "output-pcm/" //test outputs
	// "../../output-model/" //prod outputs
	// "output-pcm-JS/" //PCM prod for JS Team

	public static void main(String[] args) throws IOException {

		long startTime = System.nanoTime();

		Stream<Path> paths = Files.walk(Paths.get(pcm_path));

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
					PCM pcm = pcmContainer.getPcm();
					
					PCMInfoContainer pcmic = new PCMInfoContainer(pcm);

					
					// Il faut appeler une fonction pour déterminer
					// le detail sur les lignes
					pcm = clear_ligne(pcmic , pcm) ;
					
					
					// Il faut appeler une fonction pour déterminer
					// le detail sur les colonnes
					pcm = clear_colonne(pcmic , pcm) ;
					
					/*	filtres version lignes/colonnes
						// for using multiple filters
						PCMCompositeFilter pFilter = new PCMCompositeFilter();
						pFilter.addFilter(new PCMPredicateMinRowProduct());
						pFilter.addFilter(new PCMPredicateMinColumnProduct());
					*/
					
					/*
						Boolean isNiceLIGNE = pFilter.isSatisfiable(pcmic);

						if(!isNiceLIGNE){
							Deleteligne;
						}
					
					*/
					
					/*
					// "ligne par ligne"
					List<Product> pdts = pcm.getProducts();
					for (Product pr : pdts) {
						pr.getCells();
					}
					
					// colonne par colonne
					pcm.getFeatures();
					*/
					/*
					
						TO DO : Filtre mutant.
						
						Lignes
						
						Colones
					
					*/

					
					
					if (writefile) {
						KMFJSONExporter pcmExporter = new KMFJSONExporter();
						String pcmString = pcmExporter.export(pcmContainer);

						Path p = Paths.get(pcm_path + filePath.getFileName());
						try {
							Files.write(p, pcmString.getBytes());
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("> PCM exported to " + p);
					}
				}
			}
		});

		paths.close();

		long endTime = System.nanoTime();
		System.out.println("Took " + (endTime - startTime) / (1000000) + " ms");
		System.out.println("Took " + (endTime - startTime) / (1000000000) + " s");

	}
	
	/**
    Enlever les lignes inutiles
    @param pcmic : Le pcm info container du pcm
    @param pcm : Le pcm
    @return Le pcm avec les lignes inutiles en moins
	 */
	public static PCM clear_ligne(PCMInfoContainer pcmic , PCM pcm){
		List<Product> pdts = pcm.getProducts();
		List<Cell> cells = new ArrayList<Cell>() ;
		for (Product pr : pdts) {
			int nbCellsEmpty = 0 ;
			// On ajoute les cellules du product dans une liste
			cells = pr.getCells();
			// On traite les infos des cellules
			for(Cell c : cells){
				if(c.getContent().isEmpty()){
					nbCellsEmpty ++ ;
				}
			}
			if(nbCellsEmpty/cells.size()> 0.5){
				pcm.removeProduct(pr);
			}
			
		}
		
		return pcm;
	}
	
	/**
    Enlever les colonnes inutiles
    @param pcmic : Le pcm info container du pcm
    @param pcm : Le pcm
    @return Le pcm avec les colonnes inutiles en moins
	 */
	public static PCM clear_colonne(PCMInfoContainer pcmic , PCM pcm){
		List<Feature> pdts = pcm.getConcreteFeatures();
		List<Cell> cells = new ArrayList<Cell>() ;
		for (Feature pr : pdts) {
			int nbCellsEmpty = 0 ;
			// On ajoute les cellules du product dans une liste
			cells = pr.getCells();
			// On traite les infos des cellules
			for(Cell c : cells){
				if(c.getContent().isEmpty()){
					nbCellsEmpty ++ ;
				}
			}
			if(nbCellsEmpty/cells.size()> 0.5){
				pcm.removeFeature(pr);;
			}
			
		}
		
		return pcm;
	}

	
	
}
