package org.diverse.pcm.api.java;

import java.util.List;

public interface Product {

	String getName();
	void setName(String name);

	List<Cell> getCells();
    void addCell(Cell cell);
    void removeCell(Cell cell);
}
