package org.diverse.pcm.api.java.impl;

import java.util.ArrayList;
import java.util.List;

import org.diverse.pcm.api.java.AbstractFeature;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.api.java.util.PCMVisitor;
import pcm.FeatureGroup;
import pcm.PCM;


/**
 * Created by gbecan on 08/10/14.
 */
public class PCMImpl implements org.diverse.pcm.api.java.PCM {

    private pcm.PCM kpcm;

    public PCMImpl(pcm.PCM kpcm) {
        this.kpcm = kpcm;
    }

    public PCM getKpcm() {
        return kpcm;
    }

    @Override
    public String getName() {
        return kpcm.getName();
    }

    @Override
    public void setName(String s) {
        kpcm.setName(s);
    }

    @Override
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<Product>();
        for (pcm.Product kProduct : kpcm.getProducts()) {
            products.add(new ProductImpl(kProduct));
        }
        return products;
    }

    @Override
    public void addProduct(Product product) {
        kpcm.addProducts(((ProductImpl) product).getkProduct());
    }

    @Override
    public void removeProduct(Product product) {
        kpcm.removeProducts(((ProductImpl) product).getkProduct());
    }

    @Override
    public List<AbstractFeature> getFeatures() {
        List<AbstractFeature> features = new ArrayList<AbstractFeature>();
        for (pcm.AbstractFeature kFeature : kpcm.getFeatures()) {
            if (kFeature instanceof pcm.Feature) {
                features.add(new FeatureImpl((pcm.Feature) kFeature));
            } else if (kFeature instanceof FeatureGroup) {
                features.add(new FeatureGroupImpl((FeatureGroup) kFeature));
            }
        }
        return features;
    }

    @Override
    public void addFeature(AbstractFeature abstractFeature) {
        kpcm.addFeatures(((AbstractFeatureImpl) abstractFeature).getkAbstractFeature());
    }

    @Override
    public void removeFeature(AbstractFeature abstractFeature) {
        kpcm.removeFeatures(((AbstractFeatureImpl) abstractFeature).getkAbstractFeature());
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }
}
