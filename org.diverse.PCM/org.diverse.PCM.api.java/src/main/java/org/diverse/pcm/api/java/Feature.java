package org.diverse.pcm.api.java;

import java.util.List;

public interface Feature extends AbstractFeature {

    List<Cell> getCells();
    Cell getCell(Product product);
}
