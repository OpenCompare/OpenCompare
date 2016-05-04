package org.opencompare.api.java;

import org.opencompare.api.java.util.DiffResult;

import java.util.*;

/**
 * Created by gbecan on 6/10/15.
 */
public class PCMMetadata {

    protected PCM pcm;
    protected Boolean productAsLines;
    protected Map<Product, Integer> productPositions;
    protected Map<AbstractFeature, Integer> featurePositions;
    protected String source;
    protected String license;
    protected String creator;

    public PCMMetadata(PCM pcm) {
        this.pcm = pcm;
        this.setProductAsLines(true);
        this.productPositions = new HashMap<>();
        this.featurePositions = new HashMap<>();
        this.source = "";
        this.license = "";
        this.creator = "";
    }

    /**
     * Return the last product index used
     * @return a integer as index
     */
    public int getLastProductIndex() {
        return Collections.max(productPositions.values());
    }
    /**
     * Return the last feature index used
     * @return a integer as index
     */
    public int getLastFeatureIndex() {
        return Collections.max(featurePositions.values());
    }

    /**
     * Returns the absolute position of the product or create if not exists
     * @param product product
     * @return the absolution position of 'product' or -1 if it is not specified
     */
    public int getProductPosition(Product product) {
        return productPositions.getOrDefault(product, 0);
    }

    /**
     * Define the absolute position of the product in the PCM
     * @param product  product
     * @param position position
     */
    public void setProductPosition(Product product, int position) {
        productPositions.put(product, position);
    }

    /**
     * Returns the absolute position of the feature or create if not exists
     * @param feature
     * @return the absolution position of 'feature' or -1 if it is not specified
     */
    public int getFeaturePosition(AbstractFeature feature) {
        AbstractFeature result = feature;
        if (!featurePositions.containsKey(feature)) {
            if (feature instanceof FeatureGroup) {
                FeatureGroup featureGroup = (FeatureGroup) feature;
                List<Feature> features = featureGroup.getConcreteFeatures();
                if (!features.isEmpty()) {
                    Collections.sort(features, new Comparator<Feature>() {
                        @Override
                        public int compare(Feature feat1, Feature feat2) {
                            return getFeaturePosition(feat1) - getFeaturePosition(feat2);
                        }
                    });
                    result = features.get(0);
                }
            }
        }
        return featurePositions.getOrDefault(result, 0);
    }

    /**
     * Define the absolute position of the feature in the PCM
     * @param feature feature
     * @param position position
     */
    public void setFeaturePosition(AbstractFeature feature, int position) {
        featurePositions.put(feature, position);
    }

    /**
     * Return the sorted products concordingly with metadata
     * @return an ordered list of products
     */
    public List<Product> getSortedProducts() {

        ArrayList<Product> result = new ArrayList<>(pcm.getProducts());
        Collections.sort(result, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                Integer op1 = getProductPosition(o1);
                Integer op2 = getProductPosition(o2);
                return op1.compareTo(op2);
            }
        });
        return result;
    }

    /**
     * Return the sorted features concordingly with metadata
     * @return an ordered list of features
     */
    public List<Feature> getSortedFeatures() {
        ArrayList<Feature> result = new ArrayList<>(pcm.getConcreteFeatures());
        Collections.sort(result, new Comparator<Feature>() {
            @Override
            public int compare(Feature f1, Feature f2) {
                Integer fp1 = getFeaturePosition(f1);
                Integer fp2 = getFeaturePosition(f2);
                return fp1.compareTo(fp2);
            }
        });
        return result;
    }

    /**
     * Return the flatten hierarchy of features
     * The features are sorted with respect to the metadata
     * Feature groups are referenced multiple times to respect the hierarchy with the subfeatures
     * e.g. FG(A,B) gives FG, FG; A, B
     * @return flatten hierarchy
     */
    public List<List<AbstractFeature>> getFlattenFeatureHierarchy() {
        List<List<AbstractFeature>> result = new ArrayList<>();

        List<AbstractFeature> previousLevel = new ArrayList<>(getSortedFeatures());
        result.add(previousLevel);

        while (!previousLevel.isEmpty()) {
            List<AbstractFeature> currentLevel = new ArrayList<>();
            boolean isTopLevel = true;

            for (AbstractFeature feature : previousLevel) {
                if (feature.getParentGroup() != null) {
                    isTopLevel = false;
                    currentLevel.add(feature.getParentGroup());
                } else {
                    currentLevel.add(feature);
                }

            }

            if (!isTopLevel) {
                result.add(currentLevel);
                previousLevel = currentLevel;
            } else {
                previousLevel = new ArrayList<>();
            }
        }

        Collections.reverse(result);

        return result;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){ return true;}
        if (obj instanceof PCMMetadata) {
            PCMMetadata metadata = (PCMMetadata) obj;
            if (metadata.pcm == null || this.pcm == null) {
                return false;
            }
            if (metadata.pcm.equals(this.pcm)) {
                for (Product product : this.pcm.getProducts()) {
                    if (getProductPosition(product) != metadata.getProductPosition(product)) {
                        return false;
                    }
                }
                for (Feature feature : this.pcm.getConcreteFeatures()) {
                    if (getFeaturePosition(feature) != metadata.getFeaturePosition(feature)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String result = "PCMMetadata(\n";
        result += "product positions: {\n";
        for (Product product : productPositions.keySet()) {
            result += "\t" + product.getKeyContent() + " : " + productPositions.get(product) + ",\n";
        }
        result += "}\n";
        result += "feature positions: {\n";
        for (AbstractFeature feature : featurePositions.keySet()) {
            result += "\t" + feature.getName() + " : " + featurePositions.get(feature) + ",\n";
        }
        result += "}\n";
        result += ")";
        return result;
    }

    public PCM getPcm() {
        return pcm;
    }

    public Boolean getProductAsLines() {
        return productAsLines;
    }

    public void setProductAsLines(Boolean productAsLines) {
        this.productAsLines = productAsLines;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void clearProductPosition(Product product) {
        productPositions.remove(product);
    }

    public void clearFeaturePosition(AbstractFeature feature) {
        featurePositions.remove(feature);
    }
}
