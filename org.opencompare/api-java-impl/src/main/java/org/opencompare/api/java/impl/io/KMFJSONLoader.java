package org.opencompare.api.java.impl.io;

import org.kevoree.modeling.api.json.JsonModelLoader;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.io.PCMLoader;
import pcm.PcmModel;
import pcm.PcmUniverse;
import pcm.PcmView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


/**
 * Created by gbecan on 12/12/14.
 */
public class KMFJSONLoader implements PCMLoader {



    private JsonModelLoader loader = new JsonModelLoader();
    

    @Override
    public PCM load(String json) {
        PcmModel model = new PcmModel();
        model.connect();
        PcmUniverse universe = model.newUniverse();
        PcmView view = universe.time(0l);

        try {
            view.json().load(json).getResult();
            pcm.PCM pcm = (pcm.PCM) view.lookup(1l).getResult();
            return new PCMImpl(pcm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PCM load(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        String json = new String(bytes, StandardCharsets.UTF_8);
        return load(json);
    }
}
