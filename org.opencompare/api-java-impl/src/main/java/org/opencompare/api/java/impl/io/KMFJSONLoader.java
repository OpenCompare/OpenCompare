package org.opencompare.api.java.impl.io;

import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.json.JsonModelLoader;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.io.PCMLoader;
import pcm.PcmModel;
import pcm.PcmUniverse;
import pcm.PcmView;
import pcm.impl.PcmViewImpl;

import java.io.*;


/**
 * Created by gbecan on 12/12/14.
 */
public class KMFJSONLoader implements PCMLoader {



    private JsonModelLoader loader = new JsonModelLoader();
    

    @Override
    public PCM load(String json) {
        PcmModel model = new PcmModel();
        PcmUniverse universe = model.newUniverse();
        PcmView view = new PcmViewImpl(System.currentTimeMillis(), universe);


        JsonModelLoader.load(view, json, new Callback<Throwable>() {
            @Override
            public void on(Throwable throwable) {

            }
        });


        return load(containers);
    }

    @Override
    public PCM load(File file) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        List<KMFContainer> containers = loader.loadModelFromStream(in);

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return load(containers);

//        byte[] bytes = Files.readAllBytes(file.toPath());
//        String json = new String(bytes, StandardCharsets.UTF_8);
//        return load(json);
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
