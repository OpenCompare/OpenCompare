package org.opencompare.api.java.util;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Product;

import java.util.List;

/**
 * Created by smangin on 6/1/15.
 */
public class SimplePCMElementComparator implements PCMElementComparator {
    @Override
    public boolean similarFeature(AbstractFeature f1, AbstractFeature f2) {
        return f1.equals(f2);
    }

    @Override
    public boolean similarProduct(Product p1, Product p2) {
        return p1.getKeyCell().equals(p2.getKeyCell());
    }

    @Override
    public boolean similarCell(Cell c1, Cell c2) {
        return c1.getContent().equalsIgnoreCase(c2.getContent());
    }

    @Override
    public Product disambiguateProduct(Product product, List<Product> products) {
        return products.get(0);
    }

    @Override
    public AbstractFeature disambiguateFeature(AbstractFeature feature, List<AbstractFeature> features) {
        return features.get(0);
    }
}
