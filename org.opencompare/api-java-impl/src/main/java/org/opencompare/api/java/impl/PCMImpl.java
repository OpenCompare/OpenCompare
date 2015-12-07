package org.opencompare.api.java.impl;

import java.util.*;

import org.opencompare.api.java.*;
import org.opencompare.api.java.exception.MergeConflictException;
import org.opencompare.api.java.util.*;

/**
 * Created by gbecan on 08/10/14.
 */
public class PCMImpl implements PCM {

    private org.opencompare.model.PCM kpcm;

    public PCMImpl(org.opencompare.model.PCM kpcm) {
        this.kpcm = kpcm;
    }

    public org.opencompare.model.PCM getKpcm() {
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
        for (org.opencompare.model.Product kProduct : kpcm.getProducts()) {
            products.add(new ProductImpl(kProduct));
        }
        return products;
    }

    @Override
    public Feature getProductsKey() {
        org.opencompare.model.Feature key = kpcm.getProductsKey();
        return key == null ? null : new FeatureImpl(key);
    }

    @Override
    public void setProductsKey(Feature feature) {
        kpcm.setProductsKey(((FeatureImpl) feature).getkFeature());
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
        for (org.opencompare.model.AbstractFeature kFeature : kpcm.getFeatures()) {
            if (kFeature instanceof org.opencompare.model.Feature) {
                features.add(new FeatureImpl((org.opencompare.model.Feature) kFeature));
            } else if (kFeature instanceof org.opencompare.model.FeatureGroup) {
                features.add(new FeatureGroupImpl((org.opencompare.model.FeatureGroup) kFeature));
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
            if (product.getKeyContent().equals(name)) {
                return product;
            }
        }

        // The product does not exists, we create a new product
        Product newProduct = factory.createProduct();
        this.addProduct(newProduct);

        return newProduct;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void merge(PCM pcm, PCMFactory factory) throws MergeConflictException {
        mergeFeatures(this.getFeatures(), pcm.getFeatures(), null, factory);
        mergeProducts(pcm, factory);
        mergeCells(pcm, factory);
    }

    private void addNewFeatures(PCM pcm, PCMFactory factory) {
        for (AbstractFeature aFeature : pcm.getFeatures()) { // TODO : check usage of getFeatures()

            // Check if the feature already exists in this PCM
            boolean existInThis = false;
            for (AbstractFeature aFeatureInThis : this.getFeatures()) { // TODO : check usage of getFeatures()
                if (aFeature.equals(aFeatureInThis)) {
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

    private void mergeFeatures(List<AbstractFeature> featuresPCM1, List<AbstractFeature> featuresPCM2, FeatureGroup parent, PCMFactory factory) {

        for (AbstractFeature feature2 : featuresPCM2) {

            AbstractFeature equivalentFeature = null;

            for (AbstractFeature feature1 : featuresPCM1) {
                boolean sameFeatures = feature1.equals(feature2);
                boolean sameTypes = (feature1 instanceof Feature && feature2 instanceof Feature) ||
                        (feature1 instanceof FeatureGroup && feature2 instanceof FeatureGroup);
                if (sameFeatures && sameTypes) {
                    equivalentFeature = feature1;
                }
            }

            if (equivalentFeature != null) {
                if (equivalentFeature instanceof FeatureGroup && feature2 instanceof FeatureGroup) {
                    FeatureGroup featureGroup1 = (FeatureGroup) equivalentFeature;
                    FeatureGroup featureGroup2 = (FeatureGroup) feature2;
                    mergeFeatures(featureGroup1.getFeatures(), featureGroup2.getFeatures(), featureGroup1, factory);
                }
            } else {
                AbstractFeature feature2Copy = (AbstractFeature) feature2.clone(factory);
                if (parent == null) {
                    this.addFeature(feature2Copy);
                } else {
                    parent.addFeature(feature2Copy);
                }
            }


        }
    }

    private void mergeProducts(PCM pcm, PCMFactory factory) {


        for (Product product : pcm.getProducts()) {

            // Check if the product already exists in this PCM
            boolean existInThis = false;
            for (Product productInThis : this.getProducts()) {
                if (product.getKeyContent().equals(productInThis.getKeyContent())) {
                    existInThis = true;
                    break;
                }
            }

            // Copy product from merged PCM if the product is new
            if (!existInThis) {
                Product newProduct = factory.createProduct();
                this.addProduct(newProduct);
            }

        }

    }

    private void mergeCells(PCM pcm, PCMFactory factory) throws MergeConflictException {
        for (Product product : this.getProducts()) {
            for (Feature feature : this.getConcreteFeatures()) {

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

    private Cell findCorrespondingCell(PCM pcm, Product product, Feature feature) {
        Cell correspondingCell = null;

        // Find corresponding feature
        Feature correspondingFeature = null;
        for (Feature featureInPCM : pcm.getConcreteFeatures()) {
            if (featureInPCM.getName().equals(feature.getName())) {
                correspondingFeature = featureInPCM;
                break;
            }
        }

        // Find corresponding cell
        for (Product productInPCM : pcm.getProducts()) {
            if (productInPCM.getKeyContent().equals(product.getKeyCell())) {
                correspondingCell = productInPCM.findCell(correspondingFeature);
                break;
            }
        }

        return correspondingCell;
    }

    @Override
    public boolean isValid() {

        // List features
        List<Feature> features = getConcreteFeatures();


        // Check uniqueness of features
        Set<Feature> uniqueFeatures = new HashSet<Feature>(features);
        if (uniqueFeatures.size() != features.size()) {
            return false;
        }

        // Check uniqueness of products
        Set<Product> uniqueProducts = new HashSet<>(this.getProducts());
        if (uniqueProducts.size() != this.getProducts().size()) {
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

    @Override
    public void normalize(PCMFactory factory) {
        for (Product product : this.getProducts()) {
            for (Feature feature : this.getConcreteFeatures()) {
                if (product.findCell(feature) == null) {
                    Cell cell = factory.createCell();
                    cell.setFeature(feature);
                    cell.setContent("N/A");
                    cell.setInterpretation(factory.createNotAvailable());
                    product.addCell(cell);
                }
            }
        }
    }


    @Override
    public DiffResult diff(PCM pcm, PCMElementComparator pcmElementComparator) {

        DiffResult result = new DiffResult(this, pcm);

        List<Feature> thisFeatures = this.getConcreteFeatures();
        List<Feature> pcmFeatures = pcm.getConcreteFeatures();


        // Compare features
        Map<Feature, Feature> equivalentFeatures = diffFeatures(thisFeatures, pcmFeatures, pcmElementComparator, result);
        // FIXME : feature groups are not supported

        // Compare products
        Map<Product, Product> equivalentProducts = diffProducts(this.getProducts(), pcm.getProducts(), pcmElementComparator, result);

        // Compare cells of common products and features
        compareCells(equivalentFeatures, equivalentProducts, pcmElementComparator, result);


        return result;
    }

    /**
     * Compare the features of two PCMs
     * @param pcm1Features
     * @param pcm2Features
     * @param comparator
     * @param result
     * @return equivalent features
     */
    private Map<Feature, Feature> diffFeatures(List<Feature> pcm1Features, List<Feature> pcm2Features, PCMElementComparator comparator, DiffResult result) {
        List<Feature> commonFeatures = new ArrayList<Feature>();
        List<Feature> featuresOnlyInPCM1 = new ArrayList<Feature>();
        List<Feature> featuresOnlyInPCM2 = new ArrayList<Feature>(pcm2Features);

        Map<Feature, Feature> equivalentFeatures = new HashMap<Feature, Feature>();

        for (Feature f1 : pcm1Features) {
            boolean similarFeature = false;
            for (Feature f2 : pcm2Features) {
                similarFeature = comparator.similarFeature(f1, f2);
                if (similarFeature) {
                    commonFeatures.add(f1);
                    featuresOnlyInPCM2.remove(f2);
                    equivalentFeatures.put(f1, f2);
                    break;
                }
            }

            if (!similarFeature) {
                featuresOnlyInPCM1.add(f1);
            }
        }

        result.setCommonFeatures(commonFeatures);
        result.setFeaturesOnlyInPCM1(featuresOnlyInPCM1);
        result.setFeaturesOnlyInPCM2(featuresOnlyInPCM2);

        return equivalentFeatures;
    }

    /**
     * Compare the products of two PCMs
     * @param pcm1Products
     * @param pcm2Products
     * @param comparator
     * @param result
     * @return equivalent products
     */
    private Map<Product, Product> diffProducts(List<Product> pcm1Products, List<Product> pcm2Products, PCMElementComparator comparator, DiffResult result) {
        List<Product> commonProducts = new ArrayList<Product>();
        List<Product> productsOnlyInPCM1 = new ArrayList<Product>();
        List<Product> productsOnlyInPCM2 = new ArrayList<Product>(pcm2Products);

        Map<Product, Product> equivalentProducts = new HashMap<Product, Product>();

        for (Product p1 : pcm1Products) {

            boolean similarProduct = false;

            for (Product p2 : pcm2Products) {
                similarProduct = comparator.similarProduct(p1, p2);
                if (similarProduct) {
                    commonProducts.add(p1);
                    productsOnlyInPCM2.remove(p2);
                    equivalentProducts.put(p1, p2);
                    break;
                }
            }

            if (!similarProduct) {
                productsOnlyInPCM1.add(p1);
            }

        }

        result.setCommonProducts(commonProducts);
        result.setProductsOnlyInPCM1(productsOnlyInPCM1);
        result.setProductsOnlyInPCM2(productsOnlyInPCM2);

        return equivalentProducts;
    }

    private void compareCells(Map<Feature, Feature> equivalentFeatures, Map<Product, Product> equivalentProducts, PCMElementComparator comparator, DiffResult result) {
        List<Pair<Cell, Cell>> differingCells = new ArrayList<Pair<Cell, Cell>>();

        for (Feature f1 : result.getCommonFeatures()) {
            Feature f2 = equivalentFeatures.get(f1);

            for (Product p1 : result.getCommonProducts()) {
                Product p2 = equivalentProducts.get(p1);

                Cell c1 = p1.findCell(f1);
                Cell c2 = p2.findCell(f2);

                if (!comparator.similarCell(c1, c2)) {
                    differingCells.add(new Pair<Cell, Cell>(c1, c2));
                }
            }
        }

        result.setDifferingCells(differingCells);
    }

    @Override
    public void invert(PCMFactory factory) {
        // Save original features and products
        Feature productsKey = this.getProductsKey();
        List<Feature> originalFeatures = this.getConcreteFeatures();
        List<Product> originalProducts = this.getProducts();


        // Clean PCM
        for (AbstractFeature originalFeature : this.getFeatures()) {
            this.removeFeature(originalFeature);
        }

        for (Product originalProduct : originalProducts) {
            this.removeProduct(originalProduct);
        }

        // Restore products' key
        this.addFeature(productsKey);

        // Create new features
        Map<Product, Feature> productToFeature = new HashMap<>(); // Mapping between original products and new features
        for (Cell cell : productsKey.getCells()) {
            Feature feature = factory.createFeature();
            feature.setName(cell.getContent());
            this.addFeature(feature);

            productToFeature.put(cell.getProduct(), feature);
        }

        // Create new products and their corresponding key cell
        Map<Feature, Product> featureToProduct = new HashMap<>(); // Mapping between original features and new products
        for (Feature originalFeature : originalFeatures) {
            if (!originalFeature.equals(productsKey)) {
                Cell cell = factory.createCell();
                cell.setContent(originalFeature.getName());
                cell.setFeature(productsKey);

                Product product = factory.createProduct();
                product.addCell(cell);
                this.addProduct(product);

                featureToProduct.put(originalFeature, product);
            }
        }

        // FIXME : feature groups ???

        // Create new cells
        for (Feature originalFeature : originalFeatures) {
            if (!originalFeature.equals(productsKey)) {
                for (Cell cell : originalFeature.getCells()) {
                    Feature newFeature = productToFeature.get(cell.getProduct());
                    Product newProduct = featureToProduct.get(cell.getFeature());

                    Cell newCell = factory.createCell();
                    newCell.setContent(cell.getContent());
                    newCell.setRawContent(cell.getRawContent());
                    newCell.setInterpretation(cell.getInterpretation());
                    newCell.setFeature(newFeature);
                    newProduct.addCell(newCell);
                }
            }
        }

    }

    @Override
    public int getFeaturesDepth() {
        int depth = 1;
        for (AbstractFeature abstractFeature: getFeatures()) {
            if (abstractFeature instanceof FeatureGroup) {
                FeatureGroup featureGroup = (FeatureGroup) abstractFeature;
                int subdepth = featureGroup.getDepth();
                if (subdepth > depth) {
                    depth = subdepth;
                }
            }
        }
        return depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PCMImpl pcm = (PCMImpl) o;

        if (this.getName() == null && pcm.getName() != null) {
            return false;
        }

        if (this.getName() != null && !this.getName().equals(pcm.getName())) {
            return false;
        }

        Set<Feature> thisConcreteFeaturesSet = new HashSet<>(this.getConcreteFeatures());
        Set<Feature> pcmConcreteFeaturesSet = new HashSet<>(pcm.getConcreteFeatures());

        if (!thisConcreteFeaturesSet.equals(pcmConcreteFeaturesSet)) {
            return false;
        }

        Set<Product> thisProductSet = new HashSet<>(this.getProducts());
        Set<Product> pcmProductSet = new HashSet<>(pcm.getProducts());

        if (!thisProductSet.equals(pcmProductSet)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName(), new HashSet<Feature>(this.getConcreteFeatures()), new HashSet<Product>(this.getProducts()));
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        PCM copy = factory.createPCM();
        copy.setName(this.getName());

        for (AbstractFeature feature : this.getFeatures()) {
            copy.addFeature((AbstractFeature) feature.clone(factory));
        }

        for (Product product : this.getProducts()) {
            Product productCopy = (Product) product.clone(factory);
            copy.addProduct(productCopy);
        }

        return copy;
     }

    @Override
    public String toString() {
        return "PCMImpl{" +
                "name= " + this.getName() +
                ",#features= " + this.getConcreteFeatures().size() +
                ", #products= " + this.getProducts().size() +
                ", products' key= " + this.getProductsKey() +
                "}";
    }
}
