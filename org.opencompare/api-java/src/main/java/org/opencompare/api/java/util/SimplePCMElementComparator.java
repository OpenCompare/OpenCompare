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
        if (f1 == f2) {
            return true;
        }
        if (f1 == null || f2 == null) {
            return false;
        }
        return f1.getName().equalsIgnoreCase(f2.getName());
    }

    @Override
    public boolean similarProduct(Product p1, Product p2) {
        if (p1 == p2) {
            return true;
        }
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.getName().equals(p2.getName());
    }

    @Override
    public boolean similarCell(Cell c1, Cell c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1 == null || c2 == null) {
            return false;
        }
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
