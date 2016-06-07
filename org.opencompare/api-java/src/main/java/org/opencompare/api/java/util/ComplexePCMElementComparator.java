package org.opencompare.api.java.util;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by smangin on 6/1/15.
 */
public class ComplexePCMElementComparator implements PCMElementComparator {
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
        return p1.getKeyContent().equals(p2.getKeyContent());
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
        TreeMap<Integer, Product> mapping = new TreeMap<Integer, Product>();
        Product result = null;
        for (Cell cell1 : product.getCells()) {
            for(Product prod2 : products) {
                int i = product.getCells().size(); // To keep quantitative cell similarities
                // TODO
                for (Cell cell2 : prod2.getCells()) {
                    if (cell2.getContent().equals(cell1.getContent())) {
                        i += 1;
                    }
                }
                mapping.put(i, prod2);
            }
        }
        return mapping.pollLastEntry().getValue();
    }

    @Override
    public AbstractFeature disambiguateFeature(AbstractFeature feature, List<AbstractFeature> features) {
        TreeMap<Integer, AbstractFeature> mapping = new TreeMap<Integer, AbstractFeature>();
        //AbstractFeature result = null;
        //for (Cell cell1 : feature.getCells()) {
        //    for(AbstractFeature feat2 : features) {
        //        int i = feature.getCells().size(); // To keep quantitative cell similarities
        //        // TODO
        //        for (Cell cell2 : feat2.getCells()) {
        //            if (cell2.getContent().equals(cell1.getContent())) {
        //                i += 1;
        //            }
        //        }
        //        mapping.put(i, feat2);
        //    }
        //}
        return mapping.pollLastEntry().getValue();
    }
}
