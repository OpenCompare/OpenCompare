package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Feature;
import org.diverse.pcm.api.java.util.PCMVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbecan on 08/10/14.
 */
public class ProductImpl implements org.diverse.pcm.api.java.Product {

    private pcm.Product kProduct;

    public ProductImpl(pcm.Product kProduct) {
        this.kProduct = kProduct;
    }

    public pcm.Product getkProduct() {
        return kProduct;
    }

    @Override
    public String getName() {
        return kProduct.getName();
    }

    @Override
    public void setName(String s) {
        kProduct.setName(s);
    }

    @Override
    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<Cell>();
        for (pcm.Cell kCell : kProduct.getValues()) {
            cells.add(new CellImpl(kCell));
        }
        return cells;
    }

    @Override
    public void addCell(Cell cell) {
        kProduct.addValues(((CellImpl) cell).getkCell());
    }

    @Override
    public void removeCell(Cell cell) {
        kProduct.removeValues(((CellImpl) cell).getkCell());
    }

    @Override
    public Cell findCell(Feature feature) {
        // TODO : try to use built-in KMF features for finding a cell
//        FeatureImpl featureImpl = (FeatureImpl) feature;
//        String id = kProduct.getValues().get(0).getGenerated_KMF_ID();
//        System.out.println(kProduct.getValues());
//
//        for(pcm.Cell cell : kProduct.getValues()) {
//            System.out.println(cell.getFeature().getGenerated_KMF_ID());
//        }
//
//        String query = "values[feature/id = " + featureImpl.getkFeature().getGenerated_KMF_ID() + "]";
//        System.out.println(query);
//        System.out.println(kProduct.select(query));

        for (Cell cell : getCells()) {
            if (cell.getFeature().equals(feature)) {
                return cell;
            }
        }

        return null;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String toString() {
        return "Product(" + getName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductImpl product = (ProductImpl) o;

        return !(kProduct != null ? !kProduct.equals(product.kProduct) : product.kProduct != null);

    }

    @Override
    public int hashCode() {
        return kProduct != null ? kProduct.hashCode() : 0;
    }
}
