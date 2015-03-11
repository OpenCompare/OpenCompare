package org.diverse.pcm.api.java;

import java.util.List;

public interface Product extends PCMElement {

	String getName();
	void setName(String name);

	List<Cell> getCells();
    void addCell(Cell cell);
    void removeCell(Cell cell);

    Cell findCell(Feature feature);

}
