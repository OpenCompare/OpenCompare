package org.opencompare.api.java.impl.io;

import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.io.PCMLoader;
import org.kevoree.modeling.api.KMFContainer;
import org.kevoree.modeling.api.json.JSONModelLoader;
import pcm.factory.DefaultPcmFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbecan on 12/12/14.
 */
public class KMFJSONLoader implements PCMLoader {

    private DefaultPcmFactory kpcmFactory = new DefaultPcmFactory();
    private JSONModelLoader loader = kpcmFactory.createJSONLoader();
    private PCMBase64Encoder encoder = new PCMBase64Encoder();

    @Override
    public List<PCMContainer> load(String json) {
        List<KMFContainer> containers = loader.loadModelFromString(json);
        return load(containers);
    }

    @Override
    public List<PCMContainer> load(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        String json = new String(bytes, StandardCharsets.UTF_8);
        return load(json);
    }

    private List<PCMContainer> load(List<KMFContainer> containers) {
        List<PCMContainer> containersPCM = new ArrayList<>();
        for (KMFContainer container : containers) {
            PCM pcm = new PCMImpl((pcm.PCM) container);
            encoder.decode(pcm);
            PCMContainer containerPCM = new PCMContainer();
            PCMMetadata metadata = new PCMMetadata(pcm);
            containerPCM.setPcm(pcm);
            containerPCM.setMetadata(metadata);
            containersPCM.add(containerPCM);
        }
        return containersPCM;
    }
}

