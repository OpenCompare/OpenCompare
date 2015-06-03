package org.opencompare.api.java.util;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Product;

/**
 * Created by smangin on 6/1/15.
 */
public class SimplePCMElementComparator implements PCMElementComparator {
    @Override
    public boolean similarFeature(AbstractFeature f1, AbstractFeature f2) {
        return !f1.getName().equalsIgnoreCase(f2.getName());
    }

    @Override
    public boolean similarProduct(Product p1, Product p2) {
        return !p1.getName().equalsIgnoreCase(p2.getName());
    }

    @Override
    public boolean similarCell(Cell c1, Cell c2) {
        return !c1.getContent().equals(c2.getContent());
    }
}
