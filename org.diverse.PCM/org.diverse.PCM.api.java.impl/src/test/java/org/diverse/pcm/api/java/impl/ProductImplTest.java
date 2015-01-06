package org.diverse.pcm.api.java.impl;

import org.junit.Test;

/**
 * ProductImpl unit test class
 */
public class ProductImplTest {

    private PCMFactoryImpl factory = new PCMFactoryImpl();;
    private pcm.Product kproduct;
    private ProductImpl product = new ProductImpl(kproduct);
    private pcm.Cell kcell;
    private CellImpl cell = new CellImpl(kcell);

    @Test
    public void getkProduct() {
        // TODO : how to check pcm.Product
        //assertEquals(product.getkProduct(), kproduct);
    }

    @Test
    public void setGetNameTest() {
        //product.setName("Product");
        //assertEquals("Product", product.getName());
    }

    @Test
    public void setGetCellsTest() {
        //product.addCell(cell);
        //assertTrue(product.getCells().contains(cell));
    }

    @Test
    public void removeCellTest() {
        //product.removeCell(cell);
        //assertFalse(product.getCells().contains(cell));

    }
}
