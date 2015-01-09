package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Cell;
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
    public Cell getCell(String feature) {
        List<Cell> cell = getCells();
        for(Cell kCell : cell){
            if(kCell.getFeature().getName().equals(feature))
                return  kCell;
        }

        return  null;
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
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }
}
