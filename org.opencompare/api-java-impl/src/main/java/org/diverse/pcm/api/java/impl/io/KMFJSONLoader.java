package org.diverse.pcm.api.java.impl.io;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.impl.PCMImpl;
import org.diverse.pcm.api.java.io.PCMLoader;
import org.kevoree.modeling.api.KMFContainer;
import org.kevoree.modeling.api.json.JSONModelLoader;
import org.kevoree.modeling.api.xmi.XMIModelLoader;
import pcm.factory.DefaultPcmFactory;

import java.io.*;
import java.util.List;

/**
 * Created by gbecan on 12/12/14.
 */
public class KMFJSONLoader implements PCMLoader {

    private DefaultPcmFactory kpcmFactory = new DefaultPcmFactory();
    private JSONModelLoader loader = kpcmFactory.createJSONLoader();

    @Override
    public PCM load(String json) {
        List<KMFContainer> containers = loader.loadModelFromString(json);
        return load(containers);
    }

    @Override
    public PCM load(File file) throws FileNotFoundException {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        List<KMFContainer> containers = loader.loadModelFromStream(in);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return load(containers);

    }

    private PCM load(List<KMFContainer> containers) {
        if (containers.size() == 1 && containers.get(0) instanceof pcm.PCM) {
            return new PCMImpl((pcm.PCM) containers.get(0));
        } else {
            // FIXME : what does it mean to have several PCMs in a container?
            return null;
        }
    }
}
