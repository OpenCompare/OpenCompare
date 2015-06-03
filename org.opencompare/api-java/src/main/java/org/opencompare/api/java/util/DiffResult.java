package org.opencompare.api.java.util;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;

import java.util.List;

/**
 * Created by gbecan on 3/12/15.
 */
public class DiffResult {

    private PCM pcm1;
    private PCM pcm2;

    private List<Feature> commonFeatures;
    private List<Feature> featuresOnlyInPCM1;
    private List<Feature> featuresOnlyInPCM2;

    private List<Product> commonProducts;
    private List<Product> productsOnlyInPCM1;
    private List<Product> productsOnlyInPCM2;

    private List<Pair<Cell,Cell>> differingCells;

    public DiffResult(PCM pcm1, PCM pcm2) {
        this.pcm1 = pcm1;
        this.pcm2 = pcm2;
    }

    public PCM getPcm1() {
        return pcm1;
    }

    public PCM getPcm2() {
        return pcm2;
    }

    public List<Feature> getCommonFeatures() {
        return commonFeatures;
    }

    public void setCommonFeatures(List<Feature> commonFeatures) {
        this.commonFeatures = commonFeatures;
    }

    public List<Feature> getFeaturesOnlyInPCM1() {
        return featuresOnlyInPCM1;
    }

    public void setFeaturesOnlyInPCM1(List<Feature> featuresOnlyInPCM1) {
        this.featuresOnlyInPCM1 = featuresOnlyInPCM1;
    }

    public List<Feature> getFeaturesOnlyInPCM2() {
        return featuresOnlyInPCM2;
    }

    public void setFeaturesOnlyInPCM2(List<Feature> featuresOnlyInPCM2) {
        this.featuresOnlyInPCM2 = featuresOnlyInPCM2;
    }

    public List<Product> getCommonProducts() {
        return commonProducts;
    }

    public void setCommonProducts(List<Product> commonProducts) {
        this.commonProducts = commonProducts;
    }

    public List<Product> getProductsOnlyInPCM1() {
        return productsOnlyInPCM1;
    }

    public void setProductsOnlyInPCM1(List<Product> productsOnlyInPCM1) {
        this.productsOnlyInPCM1 = productsOnlyInPCM1;
    }

    public List<Product> getProductsOnlyInPCM2() {
        return productsOnlyInPCM2;
    }

    public void setProductsOnlyInPCM2(List<Product> productsOnlyInPCM2) {
        this.productsOnlyInPCM2 = productsOnlyInPCM2;
    }

    public List<Pair<Cell, Cell>> getDifferingCells() {
        return differingCells;
    }

    public void setDifferingCells(List<Pair<Cell, Cell>> differingCells) {
        this.differingCells = differingCells;
    }

    public Boolean hasDifferences() {
        return !this.differingCells.isEmpty()
            || !this.featuresOnlyInPCM1.isEmpty()
            || !this.featuresOnlyInPCM2.isEmpty()
            || !this.productsOnlyInPCM1.isEmpty()
            || !this.productsOnlyInPCM2.isEmpty();
    }

    public String toString() {
        String result = "PCM(" + this.getPcm1().getName() + ")\n";
        for (Product product : this.getProductsOnlyInPCM1()) {
            result += " - " + product + "\n";
        }
        for (Feature feature : this.getFeaturesOnlyInPCM1()) {
            result += " - " + feature + "\n";
        }
        result += "PCM(" + this.getPcm2().getName() + ")" + "\n";
        for (Product product : this.getProductsOnlyInPCM2()) {
            result += " - " + product + "\n";
        }
        for (Feature feature : this.getFeaturesOnlyInPCM2()) {
            result += " - " + feature + "\n";
        }
        result += "Cells differences" + "\n";
        for (Pair<Cell, Cell> cell : this.getDifferingCells()) {
            result += " 1 - " + cell._1 + " From " + cell._1.getFeature() + "\n";
            result += " 2 - " + cell._2 + " From " + cell._2.getFeature() + "\n";
        }
        return result;
    }
}
