package pcm_Metric;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.value.IntegerValueImpl;
import org.opencompare.api.java.value.Conditional;
import org.opencompare.api.java.value.IntegerValue;
import org.opencompare.api.java.value.Multiple;
import org.opencompare.api.java.value.NotAvailable;
import org.opencompare.api.java.value.RealValue;
// import org.opencompare.model.BooleanValue;


public class PCMInfoContainer implements IPCMInfoContainer {

	private static final Logger _log = Logger.getLogger(PCMInfoContainer.class.getName());

	private static final double THRESHOLD_HOMOGENEOUS = 90;
	
	private PCM _pcm;
	
	public PCMInfoContainer(PCM pcm) {
		_pcm = pcm;
	}	
	
	/* 
	public void print() {
		for (Product product : _pcm.getProducts()) {
			List<Cell> cells = product.getCells();
			for (Cell cell : cells) {
				Value v = cell.getInterpretation();
				if (v instanceof BooleanValue) {
					_log.info("boolean");
				}
			}
		}
	}
	*/
	
	@Override
	public Integer nbRows() {		
		return _pcm.getProducts().size();
	}

	@Override
	public Integer nbFeatures() {
		return _pcm.getConcreteFeatures().size();
	}

	@Override
	public Integer nbCells() {
		int nbCell = 0;
		List<Product> pdts = _pcm.getProducts();
		for (Product pdt : pdts) {
			nbCell += pdt.getCells().size();
		}
		return nbCell;
	}

	@Override
	public Integer nbEmptyCells() {
		int nbEmptyCell = 0;
		List<Product> pdts = _pcm.getProducts();
		for (Product pdt : pdts) {
			List<Cell> cells = pdt.getCells();
			for (Cell cell : cells) {
				if (cell.getContent().isEmpty())
					nbEmptyCell++;
				//if (cell.getInterpretation() instanceof NotAvailable)
				//	nbEmptyCell++;
			}
		}
		return nbEmptyCell;
	}

	@Override
	public Double ratioEmptyCells() {
		if(nbCells().equals(0)){
			return (double) 0;
			
		}
		return (double)(nbEmptyCells()*100)/nbCells();
	}

	@Override
	public Integer nbFeaturesHomog() {
		int nbFeaturesHomog =0;
		List<Feature> feats = _pcm.getConcreteFeatures();
		for(Feature feat : feats){
			
			if (feat.equals(_pcm.getProductsKey()))
				continue;
			//TreeMap<String, Integer> mapMetrics = new TreeMap();
			TreeMap<String, Integer> mapMetrics2 = new TreeMap();
			List<Cell> cells = feat.getCells();
			for(Cell cell : cells) {
				Value v = cell.getInterpretation();
				String cl = v.getClass().toString();
				if (v instanceof RealValue || v instanceof IntegerValue)
					cl = "Numeric";
					
				if (!mapMetrics2.containsKey(cl)) 
					mapMetrics2.put(cl, 0);
				else 
					mapMetrics2.put(cl, mapMetrics2.get(cl) + 1);
				
				
			} // end for cells
			Double tauxHomogeneity = tauxHomg(feat, mapMetrics2);
			System.err.println("mapMetrics2: " + mapMetrics2 + " taux=" + tauxHomogeneity);
			if(tauxHomogeneity >= THRESHOLD_HOMOGENEOUS) 
				{ 
				nbFeaturesHomog++;
				System.err.println("mapMetrics2: " + mapMetrics2 + " taux=" + tauxHomogeneity);
				}
		} //end for features	
		
		return nbFeaturesHomog;
	}
	
	

	
	public Double tauxHomg(Feature f, TreeMap<String,Integer> tmap){
		double tHomog = 0;
		if(tmap.size()==1) return tHomog=100;
			
		if(tmap.size()==0) return (double) 0;
		
		else {
			//return tmap.size() 
			
			// computeMax (the most common key)
			Set<String> keys = tmap.keySet();
			String dominantKey = "";
			int max = 0;
			for (String key : keys) {
				int nbEl = tmap.get(key).intValue();
				if (nbEl >= max) {
					max = nbEl;
					dominantKey = key;
				}
			}
			
			// we get the max and the key
			// count nb other elements
			int i = 0;
			for (String key : keys) {
				if (!key.equals(dominantKey))
					i += tmap.get(key).intValue();
					
			}
			if (i == 0)
				return 100.0;
			return (double)((double) max / (double) (max + i)) * 100;
					//(double) (tmap.get(tmap.lastKey())*100/tmap.size());		// FIXME
		}
	}
	
	@Override
	public Double ratioFeatureHomog() {
				
		if(nbFeaturesHomog().equals(0)){
			return (double) 0;
			
		}
		return (double)(nbFeaturesHomog()*100)/(nbFeatures()-1);
	}
	
	
	public Integer nbFeaturesHomogNumeric() {
		int nbFeaturesHomogNumeric =0;
		List<Feature> feats = _pcm.getConcreteFeatures();
		for(Feature feat : feats){
			
			if (feat.equals(_pcm.getProductsKey()))
				continue;
			//TreeMap<String, Integer> mapMetrics = new TreeMap();
			TreeMap<String, Integer> mapMetrics2 = new TreeMap();
			List<Cell> cells = feat.getCells();
			for(Cell cell : cells) {
				Value v = cell.getInterpretation();
				
				String cl = v.getClass().toString();
				if (v instanceof RealValue || v instanceof IntegerValue)
					cl = "Numeric";
					
				if (!mapMetrics2.containsKey(cl)) 
					mapMetrics2.put(cl, 0);
				else 
					mapMetrics2.put(cl, mapMetrics2.get(cl) + 1);
				
				
			} // end for cells
			Double tauxHomogeneity = tauxHomg(feat, mapMetrics2);
			System.err.println("mapMetrics2: " + mapMetrics2 + " taux=" + tauxHomogeneity);
			if(tauxHomogeneity >= THRESHOLD_HOMOGENEOUS) 
				{ 
				if(isFeatureNumeric(feat)){
					nbFeaturesHomogNumeric++;
					System.err.println("mapMetrics2: " + mapMetrics2 + " taux=" + tauxHomogeneity);
					}
				}
		} //end for features	
		
		return nbFeaturesHomogNumeric;
	}		
	
	/**
	 * 
	 * @param f feature homogeneous
	 * @return if the feature is numeric or not
	 */
	public boolean isFeatureNumeric(Feature f){
		Cell cell = f.getCells().get(0);
		Value v = cell.getInterpretation();
		if (v instanceof RealValue || v instanceof IntegerValue){
			return true;	
		}
		else{
			return false;
		}		
	}
	
	@Override
	public Integer score(){
		return null;
		
	}

}
