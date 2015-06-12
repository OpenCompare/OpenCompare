package org.opencompare.api.java.impl;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMTest;
import org.opencompare.api.java.impl.io.KMFJSONExporter;
import org.opencompare.api.java.impl.io.KMFJSONLoader;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by gbecan on 09/10/14.
 */
public class PCMImplTest extends PCMTest {

    @Override
    public void setUp() {
        factory = new PCMFactoryImpl();
    }

    @Test
    public void testJSONSaveAndLoad() throws IOException {

        // Create a PCM
        PCM pcm = factory.createPCM();
        pcm.setName("test");

        // Serialize
        KMFJSONExporter serializer = new KMFJSONExporter();
        String json = serializer.export(pcm);

        // Load
        KMFJSONLoader loader = new KMFJSONLoader();
        List<PCMContainer> containers = loader.load(json);

        for (PCMContainer container : containers) {
            assertEquals(pcm.getName(), container.getPcm().getName());
        }

    }
}
