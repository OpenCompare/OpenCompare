package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.PCMTest;
import org.diverse.pcm.api.java.export.PCMtoJson;
import org.diverse.pcm.api.java.impl.export.PCMtoJsonImpl;
import org.diverse.pcm.api.java.impl.io.JSONLoaderImpl;
import org.diverse.pcm.api.java.io.JSONLoader;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
        PCMtoJson serializer = new PCMtoJsonImpl();
        String json = serializer.toJson(pcm);

        System.out.println(pcm.getName());
        System.out.println(json);

        // Load
        JSONLoader loader = new JSONLoaderImpl();
        PCM loadedPCM = loader.load(json);

        assertEquals(pcm.getName(), loadedPCM.getName());

    }
}
