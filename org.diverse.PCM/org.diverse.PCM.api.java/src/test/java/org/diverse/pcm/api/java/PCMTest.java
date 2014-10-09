package org.diverse.pcm.api.java;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.diverse.pcm.api.java.value.BooleanValue;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

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

    }
}
