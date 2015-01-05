package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.impl.*;
import org.diverse.pcm.api.java.*;
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
    public void ProductValueTest() {
        assertEquals(product.getName(), "Product1");
        // TODO: check why object reference change between instanciation and affection
        //assertTrue(product.getCells().contains(cell));
    }

    @Test
    public void CellValueTest() {
        assertEquals(cell.getContent(), "Value1");
        assertEquals(cell.getFeature(), feature);
        // TODO: check why object reference change between instanciation and affection
        //assertEquals(cell.getInterpretation(), value);
    }

    @Test
    public void FeatureValueTest() {
        assertEquals(feature.getName(), "Feature1");
    }
}
