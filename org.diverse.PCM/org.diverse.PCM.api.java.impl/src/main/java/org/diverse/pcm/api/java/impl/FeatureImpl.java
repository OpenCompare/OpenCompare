package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.api.java.util.PCMVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbecan on 08/10/14.
 */
public class FeatureImpl extends AbstractFeatureImpl implements org.diverse.pcm.api.java.Feature {

    private pcm.Feature kFeature;

    public FeatureImpl(pcm.Feature kFeature) {
        super(kFeature);
        this.kFeature = kFeature;
    }

    public pcm.Feature getkFeature() {
        return kFeature;
    }

    @Override
    public String getName() {
        return kFeature.getName();
    }

    @Override
    public void setName(String s) {
        kFeature.setName(s);
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureImpl feature = (FeatureImpl) o;

        if (kFeature != null ? !kFeature.equals(feature.kFeature) : feature.kFeature != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return kFeature != null ? kFeature.hashCode() : 0;
    }

    @Override
    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<Cell>();
        for (pcm.Cell kCell : kFeature.getValues()) {
            cells.add(new CellImpl(kCell));
        }
        return cells;
    }

    @Override
    public Cell getCell(Product product) {
        for (Cell cell: getCells()) {
            if (cell.getProduct().equals(product)) {
                return cell;
            }
        }
        return null;
    }
}
