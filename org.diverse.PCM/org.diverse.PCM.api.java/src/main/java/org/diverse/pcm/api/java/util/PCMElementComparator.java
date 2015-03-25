package org.diverse.pcm.api.java.util;

import org.diverse.pcm.api.java.AbstractFeature;
import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Product;

/**
 * Created by gbecan on 3/12/15.
 */
public interface PCMElementComparator {

    boolean similarFeature(AbstractFeature f1, AbstractFeature f2);
    boolean similarProduct(Product p1, Product p2);
    boolean similarCell(Cell c1, Cell c2);

}
