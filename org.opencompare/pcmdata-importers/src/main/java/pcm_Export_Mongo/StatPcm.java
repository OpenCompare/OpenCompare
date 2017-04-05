package pcm_Export_Mongo;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.value.IntegerValue;
import org.opencompare.api.java.value.RealValue;

/**
 * contains "metrics" of PCM
 * 
 * @author macher1
 *
 */
public class StatPcm {

	private static final double THRESHOLD_HOMOGENEOUS = 90;

	private PCM pcm;

	private int nbRows;
	private int nbFeatures;
	private int nbCells;
	private int nbEmptyCells;
	private double ratioEmptyCells;
	private int nbFeaturesHomog;
	private double ratioFeaturesHomog;
	private int nbFeaturesHomogNumeric;

	public StatPcm(PCM _pcm) {
		pcm = _pcm;
		setNbRows();
		setNbFeatures();
		setNbCells();
		setNbEmptyCells();
		setRatioEmptyCells();
		setNbFeaturesHomog();
		setRatioFeaturesHomog();
		setNbFeaturesHomogNumeric();
	}

	public int getNbRows() {
		return nbRows;
	}

	public void setNbRows() {
		this.nbRows = pcm.getProducts().size();
	}

	public int getNbFeatures() {
		return nbFeatures;
	}

	public void setNbFeatures() {
		this.nbFeatures = pcm.getConcreteFeatures().size();
	}

	public int getNbCells() {
		return nbCells;
	}

	public void setNbCells() {
		int nbCellTmp = 0;
		List<Product> pdts = pcm.getProducts();
		for (Product pdt : pdts) {
			nbCellTmp += pdt.getCells().size();
		}
		this.nbCells = nbCellTmp;
	}

	public int getNbEmptyCells() {
		return nbEmptyCells;
	}

	public void setNbEmptyCells() {
		int nbEmptyCell = 0;
		List<Product> pdts = pcm.getProducts();
		for (Product pdt : pdts) {
			List<Cell> cells = pdt.getCells();
			for (Cell cell : cells) {
				if (cell.getContent().isEmpty())
					nbEmptyCell++;
				// if (cell.getInterpretation() instanceof NotAvailable)
				// nbEmptyCell++;
			}
		}
		this.nbEmptyCells = nbEmptyCell;
	}

	public double getRatioEmptyCells() {
		return ratioEmptyCells;
	}

	public void setRatioEmptyCells() {
		Double ratioEmptyCells;

		if (getNbCells() == 0) {
			ratioEmptyCells = 0.0;

		} else {
			ratioEmptyCells = (double) ((getNbEmptyCells() * 100) / getNbCells());
		}

		this.ratioEmptyCells = ratioEmptyCells;
	}

	public int getNbFeaturesHomog() {
		return nbFeaturesHomog;
	}

	public void setNbFeaturesHomog() {
		int nbFeaturesHomog = 0;
		List<Feature> feats = pcm.getConcreteFeatures();
		for (Feature feat : feats) {

			if (feat.equals(pcm.getProductsKey()))
				continue;
			// TreeMap<String, Integer> mapMetrics = new TreeMap();
			TreeMap<String, Integer> mapMetrics2 = new TreeMap();
			List<Cell> cells = feat.getCells();
			for (Cell cell : cells) {
				Value v = cell.getInterpretation();
				if(v != null) {
					String cl = v.getClass().toString();
					if (v instanceof RealValue || v instanceof IntegerValue)
						cl = "Numeric";
	
					if (!mapMetrics2.containsKey(cl))
						mapMetrics2.put(cl, 0);
					else
						mapMetrics2.put(cl, mapMetrics2.get(cl) + 1);
				}

			} // end for cells
			Double tauxHomogeneity = tauxHomg(feat, mapMetrics2);
			// System.err.println("mapMetrics2: " + mapMetrics2 + " taux=" +
			// tauxHomogeneity);
			if (tauxHomogeneity >= THRESHOLD_HOMOGENEOUS) {
				nbFeaturesHomog++;
				// System.err.println("mapMetrics2: " + mapMetrics2 + " taux=" +
				// tauxHomogeneity);
			}
		} // end for features

		this.nbFeaturesHomog = nbFeaturesHomog;
	}

	public double getRatioFeaturesHomog() {
		return ratioFeaturesHomog;
	}

	public void setRatioFeaturesHomog() {
		Double ratioFeaturesHomog;
		if (getNbFeaturesHomog() == (0)) {
			ratioFeaturesHomog = 0.0;

		}
		if (getNbFeatures() == 1) {
			this.ratioFeaturesHomog = 0.0;
		} else {
			ratioFeaturesHomog = (double) ((getNbFeaturesHomog() * 100) / (getNbFeatures() - 1));

			this.ratioFeaturesHomog = ratioFeaturesHomog;
		}
	}

	public int getNbFeaturesHomogNumeric() {
		return nbFeaturesHomogNumeric;
	}

	public void setNbFeaturesHomogNumeric() {
		int nbFeaturesHomogNumeric = 0;
		List<Feature> feats = pcm.getConcreteFeatures();
		for (Feature feat : feats) {

			if (feat.equals(pcm.getProductsKey()))
				continue;
			// TreeMap<String, Integer> mapMetrics = new TreeMap();
			TreeMap<String, Integer> mapMetrics2 = new TreeMap();
			List<Cell> cells = feat.getCells();
			for (Cell cell : cells) {
				Value v = cell.getInterpretation();
				if( v != null){
					String cl = v.getClass().toString();
					
					if (v instanceof RealValue || v instanceof IntegerValue)
						cl = "Numeric";
	
					if (!mapMetrics2.containsKey(cl))
						mapMetrics2.put(cl, 0);
					else
						mapMetrics2.put(cl, mapMetrics2.get(cl) + 1);
				}
			} // end for cells
			Double tauxHomogeneity = tauxHomg(feat, mapMetrics2);
			// System.err.println("mapMetrics2: " + mapMetrics2 + " taux=" +
			// tauxHomogeneity);
			if (tauxHomogeneity >= THRESHOLD_HOMOGENEOUS) {
				if (isFeatureNumeric(feat)) {
					nbFeaturesHomogNumeric++;
					// System.err.println("mapMetrics2: " + mapMetrics2 + "
					// taux=" + tauxHomogeneity);
				}
			}
		} // end for features
		this.nbFeaturesHomogNumeric = nbFeaturesHomogNumeric;
	}

	public double scoreProductChartable() {
		if (nbFeaturesHomogNumeric >= 2)
			return 1.0;
		else
			return 0;
	}

	public Double tauxHomg(Feature f, TreeMap<String, Integer> tmap) {
		double tHomog = 0;
		if (tmap.size() == 1)
			return tHomog = 100;

		if (tmap.size() == 0)
			return (double) 0;

		else {
			// return tmap.size()

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
			return (double) ((double) max / (double) (max + i)) * 100;
			// (double) (tmap.get(tmap.lastKey())*100/tmap.size()); // FIXME
		}
	}

	/**
	 * 
	 * @param f
	 *            feature homogeneous
	 * 
	 * @return if the feature is numeric or not
	 */
	public boolean isFeatureNumeric(Feature f) {
		Cell cell = f.getCells().get(0);
		Value v = cell.getInterpretation();
		if (v instanceof RealValue || v instanceof IntegerValue) {
			return true;
		} else {
			return false;
		}
	}

}
