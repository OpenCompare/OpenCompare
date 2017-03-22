package pcm_Metric;

/**
 * contains "metrics" of PCM
 * 
 * @author macher1
 *
 */
public class StatPcm {

	private int nbRows;
	private int nbFeatures;
	private int nbCells;
	private int nbEmptyCells;
	private double ratioEmptyCells;
	private int nbFeaturesHomog;
	private double ratioFeaturesHomog;
	private int nbFeaturesHomogNumeric;
	private int score;

	// final double nbCellsWeigth = 1.0;
	// final double nbEmptyCellsWeigth = -0.1;

	public int getNbRows() {
		return nbRows;
	}

	public void setNbRows(int nbRows) {
		this.nbRows = nbRows;
	}

	public int getNbFeatures() {
		return nbFeatures;
	}

	public void setNbFeatures(int nbFeatures) {
		this.nbFeatures = nbFeatures;
	}

	public int getNbCells() {
		return nbCells;
	}

	public void setNbCells(int nbCells) {
		this.nbCells = nbCells;
	}

	public int getNbEmptyCells() {
		return nbEmptyCells;
	}

	public void setNbEmptyCells(int nbEmptyCells) {
		this.nbEmptyCells = nbEmptyCells;
	}

	public double getRatioEmptyCells() {
		return ratioEmptyCells;
	}

	public void setRatioEmptyCells(Double ratioEmptyCells) {
		this.ratioEmptyCells = ratioEmptyCells;
	}

	public int getNbFeaturesHomog() {
		return nbFeaturesHomog;
	}

	public void setNbFeaturesHomog(int nbFeaturesHomog) {
		this.nbFeaturesHomog = nbFeaturesHomog;
	}

	public double getRatioFeaturesHomog() {
		return ratioFeaturesHomog;
	}

	public void setRatioFeaturesHomog(double ratioFeaturesHomog) {
		this.ratioFeaturesHomog = ratioFeaturesHomog;
	}

	public int getNbFeaturesHomogNumeric() {
		return nbFeaturesHomogNumeric;
	}

	public void setNbFeaturesHomogNumeric(int nbFeaturesHomogNumeric) {
		this.nbFeaturesHomogNumeric = nbFeaturesHomogNumeric;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	/*
	 * public Double scoreCell() { return (Math.sqrt(nbCells) * nbCellsWeigth);
	 * }
	 * 
	 * public Double scoreEmptyCells() { return (Math.pow(nbEmptyCells, 2) *
	 * nbEmptyCellsWeigth); }
	 */
	public Double scoreFeature() {
		Double sc = 0.0;
		if (nbFeatures < 2) {
			sc = 0.0;
		} else if (nbFeatures < 5) {
			sc = 0.25;
		} else if (nbFeatures < 10) {
			sc = 0.5;
		} else if (nbFeatures < 15) {
			sc = 0.75;
		} else if (nbFeatures < 20) {
			sc = 1.0;
		}
		return sc;
	}

	public Double scoreProduct() {
		Double sc = 0.0;
		if (nbRows < 2) {
			sc = 0.0;
		} else if (nbRows < 5) {
			sc = 0.25;
		} else if (nbRows < 10) {
			sc = 0.5;
		} else if (nbRows < 15) {
			sc = 0.75;
		} else if (nbRows < 20) {
			sc = 1.0;
		}
		return sc;
	}

	public Double scoreRatioEmptyCells() {
		Double sc = 0.0;
		if (ratioEmptyCells > 35) {
			sc = 0.0;
		} else if (ratioEmptyCells > 25) {
			sc = 0.25;
		} else if (ratioEmptyCells > 15) {
			sc = 0.5;
		} else if (ratioEmptyCells > 5) {
			sc = 0.75;
		} else if (ratioEmptyCells == 0) {
			sc = 1.0;
		}
		return sc;
	}

	public Double scoreFeatureHomog() {
		Double sc = 0.0;
		if (nbFeaturesHomog < 2) {
			sc = 0.0;
		} else if (nbFeaturesHomog < 5) {
			sc = 0.25;
		} else if (nbFeaturesHomog < 10) {
			sc = 0.5;
		} else if (nbFeaturesHomog < 15) {
			sc = 0.75;
		} else if (nbFeaturesHomog < 20) {
			sc = 1.0;
		}
		return sc;
	}

	public Double scoreRatioFeatureHomog() {
		Double sc = 0.0;
		if (ratioFeaturesHomog < 5) {
			sc = 0.0;
		} else if (ratioFeaturesHomog < 25) {
			sc = 0.25;
		} else if (ratioFeaturesHomog < 50) {
			sc = 0.5;
		} else if (ratioFeaturesHomog < 75) {
			sc = 0.75;
		} else if (ratioFeaturesHomog == 100 ) {
			sc = 1.0;
		}
		return sc;
	}

	public double scoreProductChartable() {
		if(nbFeaturesHomogNumeric>=2)
			return 1.0;
		else
			return 0;
	}
}
