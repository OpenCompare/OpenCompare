package org.opencompare.api.java;

import java.util.List;

public interface Product extends PCMElement {

    Feature getKey();
    Cell getKeyCell();
    String getKeyContent();

	List<Cell> getCells();
    void addCell(Cell cell);
    void removeCell(Cell cell);

    Cell findCell(Feature feature);

    PCM getPCM();
}
