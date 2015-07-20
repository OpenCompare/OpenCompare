package org.opencompare.api.java.impl;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMMetadata;
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
        PCMMetadata metadata = new PCMMetadata(pcm);
        PCMContainer container = new PCMContainer();
        container.setPcm(pcm);
        container.setMetadata(metadata);
        pcm.setName("test");

        // Serialize
        KMFJSONExporter serializer = new KMFJSONExporter();
        String json = serializer.export(container);

        // Load
        KMFJSONLoader loader = new KMFJSONLoader();
        List<PCMContainer> containers = loader.load(json);

        for (PCMContainer cont : containers) {
            assertEquals(pcm, cont.getPcm());
        }

    }

    @Test
    public void testUTF8Encoding() throws IOException {

        // Create PCM with non ASCII characters
        PCM pcm = factory.createPCM();
        pcm.setName("こんにちは");
        PCMContainer pcmContainer = new PCMContainer(pcm);

        // Serialize & load
        KMFJSONExporter serializer = new KMFJSONExporter();
        String serializedPCM = serializer.export(pcmContainer);

        KMFJSONLoader loader = new KMFJSONLoader();
        PCM loadedPCM = loader.load(serializedPCM).get(0).getPcm();

        assertEquals("load(serialize(pcm)).name = pcm.name", pcm.getName(), loadedPCM.getName());
    }
}
