package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Feature;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.api.java.Value;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class ModelTest {

    PCMFactoryImpl factory = new PCMFactoryImpl();
    Product product;
    Feature feature;
    Cell cell;
    Value value;

    @Before
    public void setUp() throws Exception {
        feature = factory.createFeature();
        feature.setName("Feature1");
        value = factory.createBooleanValue();
        cell = factory.createCell();
        cell.setInterpretation(value);
        cell.setFeature(feature);
        cell.setContent("Value1");
        product = factory.createProduct();
        product.addCell(cell);
        product.setName("Product1");
    }

    @Test
    public void ProductTest() {
        assertEquals(product.getName(), "Product1");
        //assertTrue(product.getCells().contains(cell));
    }

    @Test
    public void CellTest() {
        assertEquals(cell.getContent(), "Value1");
        //assertEquals(cell.getFeature(), feature);
        //assertEquals(cell.getInterpretation(), value);
    }

    @Test
    public void FeatureTest() {
        assertEquals(feature.getName(), "Feature1");
    }
}
