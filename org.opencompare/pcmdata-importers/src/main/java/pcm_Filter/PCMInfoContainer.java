package pcm_Filter;

import java.util.List;
import java.util.logging.Logger;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.value.BooleanValue;


public class PCMInfoContainer {

	private static final Logger _log = Logger.getLogger(PCMInfoContainer.class.getName());
	
	private PCM pcm;

	private int rows;
	private int columns;

	public PCMInfoContainer(PCM pcm) {
		this.pcm = pcm;
		this.print();
	}
	
	public PCMInfoContainer(PCM pcm,Boolean compute) {
		this.pcm = pcm;
		this.print();
		if(compute){
			computeRows();
			computeColumns();
		}
	}

	public void print() {
		for (Product product : pcm.getProducts()) {
			List<Cell> cells = product.getCells();
			for (Cell cell : cells) {
				Value v = cell.getInterpretation();
				if (v instanceof BooleanValue) {
					_log.info("boolean");
				}
			}
		}
	}

	public void computeRows() {
		rows = 0;
		for (Product product : pcm.getProducts()) {
			rows++;
			// System.out.println(product.getKeyContent());
		}
		// System.out.println(rows);
	}

	public void computeColumns() {
		columns = 0;
		for (AbstractFeature feature : pcm.getFeatures()) {
			columns++;
			// System.out.println(feature.getName());
		}
		// System.out.println(columns);
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
}
