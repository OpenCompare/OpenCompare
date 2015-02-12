package org.diverse.pcm.api.java.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.diverse.pcm.api.java.*;
import org.diverse.pcm.api.java.exception.MergeConflictException;
import org.diverse.pcm.api.java.util.PCMVisitor;

/**
 * Created by gbecan on 08/10/14.
 */
public class PCMImpl implements org.diverse.pcm.api.java.PCM {

    private pcm.PCM kpcm;

    public PCMImpl(pcm.PCM kpcm) {
        this.kpcm = kpcm;
    }

    public pcm.PCM getKpcm() {
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
            } else if (kFeature instanceof pcm.FeatureGroup) {
                features.add(new FeatureGroupImpl((pcm.FeatureGroup) kFeature));
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
    public List<Feature> getConcreteFeatures() {
        List<AbstractFeature> aFeatures = this.getFeatures();

        List<Feature> features = new ArrayList<Feature>();
        for (AbstractFeature aFeature : aFeatures) {
            features.addAll(getConcreteFeatures(aFeature));
        }

        return features;
    }

    private List<Feature> getConcreteFeatures(AbstractFeature aFeature) {
        List<Feature> features = new ArrayList<Feature>();

            if (aFeature instanceof FeatureGroup) {
                FeatureGroup featureGroup = (FeatureGroup) aFeature;
                for (AbstractFeature subFeature : featureGroup.getFeatures()) {
                    features.addAll(getConcreteFeatures(subFeature));
                }
            } else {
                features.add((Feature) aFeature);
            }

        return features;
    }

    @Override
    public Feature getOrCreateFeature(String name, PCMFactory factory) {

        // Return the feature if it exists
        List<Feature> features = this.getConcreteFeatures();
        for (Feature feature : features) {
            if (feature.getName().equals(name)) {
                return feature;
            }
        }

        // The feature does not exists, we create a new feature
        Feature newFeature = factory.createFeature();
        newFeature.setName(name);
        this.addFeature(newFeature);

        return newFeature;
    }

    @Override
    public Product getOrCreateProduct(String name, PCMFactory factory) {
        // Return the product if it exists
        List<Product> products = this.getProducts();
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }

        // The product does not exists, we create a new product
        Product newProduct = factory.createProduct();
        newProduct.setName(name);
        this.addProduct(newProduct);

        return newProduct;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void merge(PCM pcm, PCMFactory factory) throws MergeConflictException {
        // Add new features
        addNewFeatures(pcm, factory);


        // Add new products
        addNewProducts(pcm, factory);

        // Merge cells
        for (Product product : this.getProducts()) {
            for (AbstractFeature aFeature : this.getFeatures()) {
                if (aFeature instanceof Feature) {
                    Feature feature = (Feature) aFeature;

                    Cell cellInThis = product.findCell(feature);
                    Cell cellInPCM = findCorrespondingCell(pcm, product, feature);

                    if (cellInThis == null && cellInPCM == null) {
                        // Create empty cell
                        Cell newCell = factory.createCell();
                        newCell.setContent("N/A");
                        newCell.setFeature(feature);
                        newCell.setInterpretation(factory.createNotAvailable());
                        product.addCell(newCell);
                    } else if (cellInThis == null) {
                        // Copy cell from 'pcm'
                        Cell newCell = factory.createCell();
                        newCell.setContent(cellInPCM.getContent());
                        newCell.setFeature(feature);
                        // TODO : copy interpretation
                        product.addCell(newCell);
                    } else if (cellInPCM == null) {
                        // Nothing to do
                    } else if (cellInThis.getContent().equals(cellInPCM.getContent())) {
                        // Nothing to do
                    } else {
                        // Conflict
                        throw new MergeConflictException();
                    }

                }
            }
        }

    }

    private void addNewFeatures(PCM pcm, PCMFactory factory) {
        for (AbstractFeature aFeature : pcm.getFeatures()) {

            // Check if the feature already exists in this PCM
            boolean existInThis = false;
            for (AbstractFeature aFeatureInThis : this.getFeatures()) {
                if (aFeature.getName().equals(aFeatureInThis.getName())) {
                    existInThis = true;
                    break;
                }
            }

            // Copy feature from merged PCM if the feature is new
            if (!existInThis) {
                AbstractFeature newFeature;
                if (aFeature instanceof Feature) {
                    newFeature = factory.createFeature();
                } else {
                    newFeature = factory.createFeatureGroup();
                    // TODO : handle sub features
                }
                newFeature.setName(aFeature.getName());

                this.addFeature(newFeature);
            }
        }
    }

    private void addNewProducts(PCM pcm, PCMFactory factory) {


        for (Product product : pcm.getProducts()) {

            // Check if the product already exists in this PCM
            boolean existInThis = false;
            for (Product productInThis : this.getProducts()) {
                if (product.getName().equals(productInThis.getName())) {
                    existInThis = true;
                    break;
                }
            }

            // Copy product from merged PCM if the product is new
            if (!existInThis) {
                Product newProduct = factory.createProduct();
                newProduct.setName(product.getName());
                this.addProduct(newProduct);
            }

        }

    }

    private Cell findCorrespondingCell(PCM pcm, Product product, Feature feature) {
        Cell correspondingCell = null;

        // Find corresponding feature
        Feature correspondingFeature = null;
        for (AbstractFeature aFeatureInPCM : pcm.getFeatures()) {
            if (aFeatureInPCM.getName().equals(feature.getName()) && aFeatureInPCM instanceof Feature) {
                Feature featureInPCM = (Feature) aFeatureInPCM;
                correspondingFeature = featureInPCM;
                break;
            }
        }

        // Find corresponding cell
        for (Product productInPCM : pcm.getProducts()) {
            if (productInPCM.getName().equals(product.getName())) {
                correspondingCell = productInPCM.findCell(correspondingFeature);
                break;
            }
        }

        return correspondingCell;
    }

    @Override
    public boolean isValid() {

        // List features
        List<Feature> features = new ArrayList<Feature>();
        for (AbstractFeature aFeature : this.getFeatures()) {
            if (aFeature instanceof Feature) {
                features.add((Feature) aFeature);
            }
        }

        // Check uniqueness of feature names
        Set<String> featureNames = new HashSet<String>();
        for (Feature feature : features) {
            featureNames.add(feature.getName());
        }
        if (featureNames.size() != features.size()) {
            return false;
        }

        // Check uniqueness of product names
        Set<String> productNames = new HashSet<String>();
        for (Product product : this.getProducts()) {
            productNames.add(product.getName());
        }
        if (productNames.size() != this.getProducts().size()) {
            return false;
        }

        // Check that a cell exists for each pair of products and features.
        for (Product product : this.getProducts()) {
            for (Feature feature : features) {
                if (product.findCell(feature) == null) {
                    return false;
                }
            }
        }

        return true;
    }
}
