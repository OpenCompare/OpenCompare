package org.opencompare.api.java.io;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMFactory;

import java.io.*;
import java.util.List;

/**
 * Created by gbecan on 5/26/15.
 */
public abstract class CSVLoaderTest {

    protected PCMFactory factory;

    @Before
    public abstract void setUp();

    @Test
    public void testAMDProcessors() throws IOException {
        InputStream inputCSVStream = getClass().getResourceAsStream("/csv/Comparison_of_AMD_processors.csv");
        CSVLoader loader = new CSVLoader(factory);
        List<PCMContainer> containers = loader.load(new InputStreamReader(inputCSVStream));
        inputCSVStream.close();
        for (PCMContainer container : containers) {
            PCM pcm = container.getPcm();
            Assert.assertNotEquals("number of features", 0, pcm.getFeatures().size());
            Assert.assertNotEquals("number of products", 0, pcm.getProducts().size());
        }
    }

    @Test
    public void testAMDProcessorsInverted() throws IOException {
        InputStream inputCSVStream = getClass().getResourceAsStream("/csv/Comparison_of_AMD_processors.csv");
        CSVLoader loader = new CSVLoader(factory, false);
        List<PCMContainer> containers = loader.load(new InputStreamReader(inputCSVStream));
        inputCSVStream.close();
        for (PCMContainer container : containers) {
            PCM pcm = container.getPcm();
            Assert.assertNotEquals("number of features", 0, pcm.getFeatures().size());
            Assert.assertNotEquals("number of products", 0, pcm.getProducts().size());
        }
    }
}
