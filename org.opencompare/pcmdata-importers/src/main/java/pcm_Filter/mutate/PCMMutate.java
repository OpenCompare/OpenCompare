package pcm_Filter.mutate;

import java.util.ArrayList;
import java.util.List;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.PCMMetadata;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.exception.MergeConflictException;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.util.DiffResult;
import org.opencompare.api.java.util.PCMElementComparator;
import org.opencompare.api.java.util.PCMVisitor;


public class PCMMutate {
	
	private static final double RATIO_EMPTY_CELL = 0.001;
	
	public static PCM Mutate(PCM pcm){
		
		PCMFactoryImpl factory = new PCMFactoryImpl();
		PCM pcm_return = factory.createPCM() ;
		

		PCMMetadata meta = new PCMMetadata(pcm);
		PCMContainer pcmC = new PCMContainer(pcm_return);
		
		pcmC.setMetadata(meta);
		pcm_return.setName(pcm.getName());
		// Il faut appeler une fonction pour déterminer
		// le detail sur les lignes
		pcm_return = clear_ligne(pcm,pcm_return) ;
		
		// Il faut appeler une fonction pour déterminer
		// le detail sur les colonnes
		pcm_return = clear_colonne(pcm,pcm_return) ;
		
		return pcm_return ;
	}
	
	/**
    Enlever les lignes inutiles
    @param pcm : Le pcm
    @return Le pcm avec les lignes inutiles en moins
	 */
	private static PCM clear_ligne(PCM pcm, PCM pcm_return){
		List<Product> pdts = pcm.getProducts();
		List<Cell> cells = new ArrayList<Cell>() ;
		

		
		for (Product pr : pdts) {
			float nbCellsEmpty = 0 ;
			// On ajoute les cellules du product dans une liste
			cells = pr.getCells();
			// On traite les infos des cellules
			for(Cell c : cells){
				if(c.getContent().isEmpty()){
					nbCellsEmpty ++ ;
				}
			}
			if(cells.size() != 0){
				System.out.println("Dans les lignes -- > \n Nombre de cellule vide :" + nbCellsEmpty + "\n Nombre de cellule : " + cells.size());
				System.out.println("Valeur du if : " + nbCellsEmpty/cells.size());
				if(!((nbCellsEmpty/cells.size()) > RATIO_EMPTY_CELL)){
					System.out.println("on ajoute la ligne !");
					pcm_return.addProduct(pr);
				}
			}			
		}
		
		return pcm_return;
	}
	
	/**
    Enlever les colonnes inutiles
    @param pcmic : Le pcm info container du pcm
    @param pcm : Le pcm
    @return Le pcm avec les colonnes inutiles en moins
	 */
	private static PCM clear_colonne(PCM pcm, PCM pcm_return){
		List<Feature> pdts = pcm.getConcreteFeatures();
		List<Cell> cells = new ArrayList<Cell>() ;
		for (Feature pr : pdts) {
			float nbCellsEmpty = 0 ;
			// On ajoute les cellules du product dans une liste
			cells = pr.getCells();
			// On traite les infos des cellules
			for(Cell c : cells){
				if(c.getContent().isEmpty()){
					nbCellsEmpty ++ ;
				}
			}
			if(cells.size() != 0){
				System.out.println("Dans les colonnes -- > \n Nombre de cellule vide :" + nbCellsEmpty + "\n Nombre de cellule : " + cells.size());
				System.out.println("Valeur du if : " + nbCellsEmpty/cells.size());
				if(!((nbCellsEmpty/cells.size()) > RATIO_EMPTY_CELL)){
					System.out.println("on ajoute la colonne !");
					pcm_return.addFeature(pr);;
				}
			}
		}
		
		return pcm_return;
	}

}
