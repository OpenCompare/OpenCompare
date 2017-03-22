package pcm_Metric;

public interface IPCMInfoContainer {

	public Integer nbRows();

	public Integer nbFeatures();

	public Integer nbCells();

	public Integer nbEmptyCells();

	public Double ratioEmptyCells();

	public Integer nbFeaturesHomog();

	public Double ratioFeatureHomog();

	public Integer score();

	// TODO

	// public int nbUcompleteProducts;
	// private boolean same_val ;

}
