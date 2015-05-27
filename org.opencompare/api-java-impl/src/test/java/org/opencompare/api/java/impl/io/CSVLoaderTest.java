package org.opencompare.api.java.impl.io;

import org.opencompare.api.java.impl.PCMFactoryImpl;

/**
 * Created by gbecan on 5/26/15.
 */
public class CSVLoaderTest extends org.opencompare.api.java.io.CSVLoaderTest {

    @Override
    public void setUp() {
        factory = new PCMFactoryImpl();
    }
}
