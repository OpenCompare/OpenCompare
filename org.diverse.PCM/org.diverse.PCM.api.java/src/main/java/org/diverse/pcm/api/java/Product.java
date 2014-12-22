package org.diverse.pcm.api.java;

import java.util.List;

public interface Product extends PCMElement {

	String getName();
	void setName(String name);

	List<Cell> getCells();
    void addCell(Cell cell);
    void removeCell(Cell cell);

	/**
	 * This  method return us the value of cell of feature
	 * @param feature
	 * @return
	 */
	Cell getCell(String feature);



}
