package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Cell;

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
}
