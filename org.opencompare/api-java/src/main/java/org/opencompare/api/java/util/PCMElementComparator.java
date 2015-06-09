package org.opencompare.api.java.util;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Product;

import java.util.List;

/**
 * Created by gbecan on 3/12/15.
 */
public interface PCMElementComparator {

    boolean similarFeature(AbstractFeature f1, AbstractFeature f2);
    boolean similarProduct(Product p1, Product p2);
    boolean similarCell(Cell c1, Cell c2);
    Product disambiguateProduct(Product product, List<Product> products);
    AbstractFeature disambiguateFeature(AbstractFeature feature, List<AbstractFeature> features);

}
