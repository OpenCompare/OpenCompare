package org.opencompare.api.java;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.opencompare.api.java.exception.MergeConflictException;
import org.opencompare.api.java.util.DiffResult;
import org.opencompare.api.java.util.PCMElementComparator;
import org.opencompare.api.java.util.SimplePCMElementComparator;
import org.opencompare.api.java.value.BooleanValue;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by gbecan on 09/10/14.
 */
public abstract class PCMTest {

    protected PCMFactory factory;

    // Utils functions
    public Feature createFeature(PCM pcm, String name) {
        Feature feature = factory.createFeature();
        feature.setName(name);
        pcm.addFeature(feature);
        return feature;
    }

    public FeatureGroup createFeatureGroup(PCM pcm, String name) {
        FeatureGroup featureGroup = factory.createFeatureGroup();
        featureGroup.setName(name);
        pcm.addFeature(featureGroup);
        return featureGroup;
    }

    public Product createProduct(PCM pcm, String name) {
        Product product = factory.createProduct();
        product.setName(name);
        pcm.addProduct(product);
        return product;
    }

    public Cell createCell(Product product, Feature feature, String content, Value interpretation) {
        Cell cell = factory.createCell();
        cell.setFeature(feature);
        cell.setContent(content);
        cell.setInterpretation(interpretation);
        product.addCell(cell);
        return cell;
    }

    @Before
    public abstract void setUp();


    @Test
    public void testPCM() {
        PCM pcm = factory.createPCM();

        assertEquals(pcm.getFeatures().size(), 0);
        assertEquals(pcm.getProducts().size(), 0);

        pcm.setName("pcm name");
        assertEquals(pcm.getName(), "pcm name");

        pcm.addFeature(factory.createFeature());
        pcm.addFeature(factory.createFeatureGroup());
        assertEquals(pcm.getFeatures().size(), 2);

        pcm.removeFeature(pcm.getFeatures().get(0));
        assertEquals(pcm.getFeatures().size(), 1);

        pcm.addProduct(factory.createProduct());
        assertEquals(pcm.getProducts().size(), 1);

        pcm.removeProduct(pcm.getProducts().get(0));
        assertEquals(pcm.getProducts().size(), 0);

    }

    @Test
    public void testFeature() throws Exception {
        Feature feature = factory.createFeature();
        feature.setName("feature name");
        assertEquals(feature.getName(), "feature name");
    }

    @Test
    public void testFeatureGroup() throws Exception {
        FeatureGroup featureGroup = factory.createFeatureGroup();
        assertEquals(featureGroup.getFeatures().size(), 0);

        featureGroup.setName("feature group name");
        assertEquals(featureGroup.getName(), "feature group name");

        Feature feature = factory.createFeature();
        featureGroup.addFeature(feature);
        featureGroup.addFeature(factory.createFeatureGroup());
        assertEquals(featureGroup.getFeatures().size(), 2);

        featureGroup.removeFeature(feature);
        assertEquals(featureGroup.getFeatures().size(), 1);
        assertThat(featureGroup.getFeatures().get(0), CoreMatchers.instanceOf(FeatureGroup.class));
    }

    @Test
    public void testProduct() throws Exception {
        Product product = factory.createProduct();
        assertEquals(product.getCells().size(), 0);

        product.setName("product name");
        assertEquals(product.getName(), "product name");

        Cell cell = factory.createCell();
        product.addCell(cell);
        assertEquals(product.getCells().size(), 1);

        product.removeCell(cell);
        assertEquals(product.getCells().size(), 0);
    }

    @Test
    public void testCell() throws Exception {
        Cell cell = factory.createCell();

        cell.setContent("content");
        assertEquals(cell.getContent(), "content");

        BooleanValue value = factory.createBooleanValue();
        value.setValue(true);
        cell.setInterpretation(value);

        assertThat(cell.getInterpretation(), CoreMatchers.instanceOf(BooleanValue.class));
        assertEquals(((BooleanValue) cell.getInterpretation()).getValue(), true);

        Feature feature = factory.createFeature();
        feature.setName("feature name");
        cell.setFeature(feature);
        assertEquals(cell.getFeature().getName(), "feature name");


    }

    @Test
    public void testMerge() throws MergeConflictException {
        // Create PCM 1
        PCM pcm1 = factory.createPCM();

        Feature commonFeature1 = createFeature(pcm1, "Common feature");
        Feature feature1 = createFeature(pcm1, "Feature from PCM 1");

        Product commonProduct1 = createProduct(pcm1, "Common product");
        createCell(commonProduct1, commonFeature1, "", null);
        createCell(commonProduct1, feature1, "", null);

        Product product1 = createProduct(pcm1, "Product from PCM 1");
        createCell(product1, commonFeature1, "", null);
        createCell(product1, feature1, "", null);



        // Create PCM 2
        PCM pcm2 = factory.createPCM();

        Feature commonFeature2 = createFeature(pcm2, "Common feature");
        Feature feature2 = createFeature(pcm2, "Feature from PCM 2");

        Product commonProduct2 = createProduct(pcm2, "Common product");
        createCell(commonProduct2, commonFeature2, "", null);
        createCell(commonProduct2, feature2, "", null);

        Product product2 = createProduct(pcm2, "Product from PCM 2");
        createCell(product2, commonFeature2, "", null);
        createCell(product2, feature2, "", null);

        // Merge PCM 1 and 2
        pcm1.merge(pcm2, factory);

        // Check resulting PCM
        assertEquals("number of features", 3, pcm1.getFeatures().size());
        assertEquals("number of products", 3, pcm1.getProducts().size());
        for (Product product : pcm1.getProducts()) {
            assertEquals("number of cells", 3, product.getCells().size());
        }

    }

    @Test
    public void testIsValid() {
        PCM pcm = factory.createPCM();

        Feature f1 = createFeature(pcm, "F1");
        Feature f2 = createFeature(pcm, "F2");
        assertTrue(pcm.isValid());

        Feature f3 = createFeature(pcm, "F2");
        assertFalse(pcm.isValid()); // Duplicated feature

        pcm.removeFeature(f3);
        assertTrue(pcm.isValid());

        Product p1 = createProduct(pcm, "P1");
        createCell(p1, f1, "C11", null);
        assertFalse(pcm.isValid()); // Missing cell

        createCell(p1, f2, "C12", null);
        assertTrue(pcm.isValid());

        Product p2 = createProduct(pcm, "P2");
        createCell(p2, f1, "C21", null);
        assertFalse(pcm.isValid()); // Missing cell

        createCell(p2, f2, "C22", null);
        assertTrue(pcm.isValid());

        Product p3 = createProduct(pcm, "P2");
        createCell(p3, f1, "C21", null);
        createCell(p3, f2, "C22", null);
        assertFalse(pcm.isValid()); // Duplicated product

        pcm.removeProduct(p3);
        assertTrue(pcm.isValid());
    }



    @Test
    public void testGetConcreteFeatures() {
        PCM pcm = factory.createPCM();
        createFeature(pcm, "Top level feature");
        FeatureGroup group = createFeatureGroup(pcm, "FG");

        Feature subFeature1 = factory.createFeature();
        subFeature1.setName("Sub feature 1");
        group.addFeature(subFeature1);

        Feature subFeature2 = factory.createFeature();
        subFeature2.setName("Sub feature 2");
        group.addFeature(subFeature2);

        assertEquals("number of top level abstract features", 2, pcm.getFeatures().size());
        assertEquals("number of features", 3, pcm.getConcreteFeatures().size());
    }


    @Test
    public void testDiff() {
        // Create PCM 1
        PCM pcm1 = factory.createPCM();

        Feature commonFeature1 = createFeature(pcm1, "Common feature");
        Feature feature1 = createFeature(pcm1, "Feature from PCM 1");

        Product commonProduct1 = createProduct(pcm1, "Common product");
        createCell(commonProduct1, commonFeature1, "common cell 1", null);
        createCell(commonProduct1, feature1, "", null);

        Product product1 = createProduct(pcm1, "Product from PCM 1");
        createCell(product1, commonFeature1, "", null);
        createCell(product1, feature1, "", null);



        // Create PCM 2
        PCM pcm2 = factory.createPCM();

        Feature commonFeature2 = createFeature(pcm2, "Common feature");
        Feature feature2 = createFeature(pcm2, "Feature from PCM 2");

        Product commonProduct2 = createProduct(pcm2, "Common product");
        createCell(commonProduct2, commonFeature2, "common cell 2", null);
        createCell(commonProduct2, feature2, "", null);

        Product product2 = createProduct(pcm2, "Product from PCM 2");
        createCell(product2, commonFeature2, "", null);
        createCell(product2, feature2, "", null);


        // Diff
        DiffResult diffResult = pcm1.diff(pcm2, new SimplePCMElementComparator());


        assertEquals("common features", 1, diffResult.getCommonFeatures().size());
        assertEquals("features only in PCM 1", 1, diffResult.getFeaturesOnlyInPCM1().size());
        assertEquals("features only in PCM 2", 1, diffResult.getFeaturesOnlyInPCM2().size());

        assertEquals("common products", 1, diffResult.getCommonProducts().size());
        assertEquals("products only in PCM 1", 1, diffResult.getProductsOnlyInPCM1().size());
        assertEquals("products only in PCM 2", 1, diffResult.getProductsOnlyInPCM2().size());

        assertEquals("differing cells", 1, diffResult.getDifferingCells().size());
    }

    @Test
    public void testInvert() {
        // Create PCM
        PCM pcm = factory.createPCM();

        Feature f1 = createFeature(pcm, "F1");
        Feature f2 = createFeature(pcm, "F2");

        Product p1 = createProduct(pcm, "P1");
        Product p2 = createProduct(pcm, "P2");

        createCell(p1, f1, "CP1F1", null);
        createCell(p1, f2, "CP1F2", null);
        createCell(p2, f1, "CP2F1", null);
        createCell(p2, f2, "CP2F2", null);

        // Check inverted PCM
        pcm.invert(factory);

        assertEquals("#features", 2, pcm.getConcreteFeatures().size());
        assertEquals("#products", 2, pcm.getProducts().size());
        for (Product product : pcm.getProducts()) {
            assertTrue("new product is an original feature", product.getName().startsWith("F"));

            for (Cell cell : product.getCells()) {
                assertTrue("new feature is an original product", cell.getFeature().getName().startsWith("P"));
                assertEquals("cell name", "C" + cell.getFeature().getName() + product.getName(), cell.getContent());
            }
        }
    }

    @Test
    public void testGetCellsFromFeature() {
        PCM pcm = factory.createPCM();
        Feature feature = createFeature(pcm, "feature");

        Product p1 = createProduct(pcm, "p1");
        Product p2 = createProduct(pcm, "p2");

        createCell(p1, feature, "C1", null);
        createCell(p2, feature, "C2", null);

        List<Cell> cells = feature.getCells();
        assertEquals("get cells from feature", 2, cells.size());

    }

}
