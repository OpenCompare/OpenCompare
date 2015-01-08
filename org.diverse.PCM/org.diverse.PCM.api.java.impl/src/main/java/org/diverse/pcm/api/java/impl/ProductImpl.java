package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.AbstractFeature;
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
    public Cell getCell(AbstractFeature abstractFeature) {
        for (Cell cell: getCells()) {
            if (cell.getFeature().equals(abstractFeature)) {
                return cell;
            }
        }
        return null;
    }

    @Override
    public Feature getFeature(Cell cell) {
        for (Cell cell_: getCells()) {
            if (cell_.equals(cell)) {
                return cell_.getFeature();
            }
        }
        return null;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductImpl product = (ProductImpl) o;

        if (kProduct != null ? !kProduct.equals(product.kProduct) : product.kProduct != null) {
            return false;
        }

        return true;
    }
}
