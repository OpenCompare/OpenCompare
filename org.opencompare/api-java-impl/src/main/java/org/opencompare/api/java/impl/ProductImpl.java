package org.opencompare.api.java.impl;

import org.opencompare.api.java.*;
import org.opencompare.api.java.util.PCMVisitor;

import java.util.*;

/**
 * Created by gbecan on 08/10/14.
 */
public class ProductImpl implements Product {

    private org.opencompare.model.Product kProduct;

    public ProductImpl(org.opencompare.model.Product kProduct) {
        this.kProduct = kProduct;
    }

    public org.opencompare.model.Product getkProduct() {
        return kProduct;
    }

    @Override
    public Feature getKey() {
        PCM pcm = getPCM();
        return pcm == null ? null : pcm.getProductsKey();
    }

    @Override
    public Cell getKeyCell() {
        return findCell(getKey());
    }

    @Override
    public String getKeyContent() {
        Cell keyCell = getKeyCell();
        return keyCell == null ? null : keyCell.getContent();
    }

    @Override
    public PCM getPCM() {
        org.opencompare.model.PCM kPCM = kProduct.getPcm();
        return kPCM == null ? null : new PCMImpl(kPCM);
    }

    @Override
    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<Cell>();
        for (org.opencompare.model.Cell kCell : kProduct.getCells()) {
            cells.add(new CellImpl(kCell));
        }
        return cells;
    }

    @Override
    public void addCell(Cell cell) {
        kProduct.addCells(((CellImpl) cell).getkCell());
    }

    @Override
    public void removeCell(Cell cell) {
        kProduct.removeCells(((CellImpl) cell).getkCell());
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

        if (feature != null) {
            for (Cell cell : getCells()) {
                if (feature.equals(cell.getFeature())) {
                    return cell;
                }
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
        StringBuilder result = new StringBuilder();
        result.append("Product(" + getKeyContent() + ")");
        result.append("(\n");
        for (Cell cell : this.getCells()) {
            result.append(cell.toString() + "\n");
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductImpl product = (ProductImpl) o;

        Set<Cell> thisCellsSet = new HashSet<>(this.getCells());
        Set<Cell> productCellsSet = new HashSet<>(product.getCells());

        if (!thisCellsSet.equals(productCellsSet)) {
            return false;
        }

        return true;

    }

    @Override
    public int hashCode() {
        return Objects.hash(new HashSet<Cell>(this.getCells()));
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        Product copy = factory.createProduct();
        for (Cell cell : this.getCells()) {
            copy.addCell((Cell) cell.clone(factory));
        }
        return copy;
    }
}
