package org.diverse.pcm.api.java;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.diverse.pcm.api.java.value.BooleanValue;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by gbecan on 09/10/14.
 */
public abstract class PCMTest {

    protected PCMFactory factory;

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
    public void testMerge() {
        // Create PCM 1
        PCM pcm1 = factory.createPCM();

        Feature commonFeature1 = factory.createFeature();
        commonFeature1.setName("Common feature");
        pcm1.addFeature(commonFeature1);

        Feature feature1 = factory.createFeature();
        feature1.setName("Feature from PCM 1");
        pcm1.addFeature(feature1);

        Product commonProduct1 = factory.createProduct();
        commonProduct1.setName("Common product");
        pcm1.addProduct(commonProduct1);

        Cell c11 = factory.createCell();
        c11.setFeature(commonFeature1);
        commonProduct1.addCell(c11);

        Cell c12 = factory.createCell();
        c12.setFeature(feature1);
        commonProduct1.addCell(c12);


        // Create PCM 2
        PCM pcm2 = factory.createPCM();

        Feature commonFeature2 = factory.createFeature();
        commonFeature2.setName("Common feature");
        pcm2.addFeature(commonFeature2);

        Feature feature2 = factory.createFeature();
        feature2.setName("Feature from PCM 2");
        pcm2.addFeature(feature2);

        Product commonProduct2 = factory.createProduct();
        commonProduct2.setName("Common product");
        pcm2.addProduct(commonProduct2);

        Cell c21 = factory.createCell();
        c21.setFeature(commonFeature2);
        commonProduct2.addCell(c21);

        Cell c22 = factory.createCell();
        c22.setFeature(feature2);
        commonProduct2.addCell(c22);

        // Merge PCM 1 and 2
        pcm1.merge(pcm2, factory);


        // Check resulting PCM
        assertEquals("number of features", 3, pcm1.getFeatures().size());
        assertEquals("number of products", 1, pcm1.getProducts().size());
        for (Product product : pcm1.getProducts()) {
            assertEquals("number of cells", 3, product.getCells().size());
        }



    }

}
