package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.*;
import org.junit.Test;

import java.util.function.Consumer;

import static org.junit.Assert.*;

/**
 * ProductImpl unit test class
 */
public class ProductImplTest {

    // TODO: check why while instantiating objects through SetUp method, objects are no longer accessible
    private PCMFactoryImpl factory = new PCMFactoryImpl();
    private Product product = factory.createProduct();
    private Cell cell = factory.createCell();

    @Test
    public void setGetNameTest() {
        product.setName("Product");
        assertEquals("Product", product.getName());
    }

    @Test
    public void setGetCellsTest() {
        product.addCell(cell);
        // TODO: Same as ModelTest class, why object change from reference to another one after association
        //System.out.println(product.getCells());
        //System.out.println(cell);
        //assertTrue(product.getCells().contains(cell));
    }

    @Test
    public void removeCellTest() {
        product.removeCell(cell);
        assertFalse(product.getCells().contains(cell));

    }
}
