package org.opencompare.api.java.impl;

import java.util.*;

import org.opencompare.api.java.*;
import org.opencompare.api.java.exception.MergeConflictException;
import org.opencompare.api.java.util.*;

/**
 * Created by gbecan on 08/10/14.
 */
public class PCMImpl implements PCM {

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
            for (AbstractFeature aFeature : this.getFeatures()) { // TODO : check usage of getFeatures()
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
        for (AbstractFeature aFeature : pcm.getFeatures()) { // TODO : check usage of getFeatures()

            // Check if the feature already exists in this PCM
            boolean existInThis = false;
            for (AbstractFeature aFeatureInThis : this.getFeatures()) { // TODO : check usage of getFeatures()
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
        for (Feature featureInPCM : pcm.getConcreteFeatures()) {
            if (featureInPCM.getName().equals(feature.getName())) {
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
        List<Feature> features = getConcreteFeatures();

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
        // FIXME : feature groups ???

        // Save original features and products
        List<Feature> originalFeatures = this.getConcreteFeatures();
        List<Product> originalProducts = this.getProducts();

        Map<Feature, Product> featureToProduct = new HashMap<Feature, Product>(); // Mapping between original features and new products

        for (Product originalProduct : originalProducts) {
            // Remove original product
            this.removeProduct(originalProduct);

            // Create new feature
            Feature newFeature = factory.createFeature();
            newFeature.setName(originalProduct.getName());
            this.addFeature(newFeature);

            // Bind cells to this new feature and a new product
            for (Cell cell : originalProduct.getCells()) {
                Feature originalFeature = cell.getFeature();
                Product newProduct = featureToProduct.get(originalFeature);

                if (newProduct == null) {
                    // Remove original feature
                    this.removeFeature(originalFeature);

                    // Create new product
                    newProduct = factory.createProduct();
                    newProduct.setName(originalFeature.getName());
                    this.addProduct(newProduct);

                    // Update mapping between original features and new products
                    featureToProduct.put(originalFeature, newProduct);
                }

                // Bind cell to its new feature and product
                newProduct.addCell(cell);
                cell.setFeature(newFeature);
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
        return !this.diff(pcm, new ComplexePCMElementComparator()).hasDifferences();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName(), this.getFeatures(), this.getProducts());
    }
}
