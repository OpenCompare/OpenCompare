package org.diverse.pcm.api.java.impl.io;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.impl.PCMImpl;
import org.diverse.pcm.api.java.io.PCMExporter;
import org.kevoree.modeling.api.json.JSONModelSerializer;
import pcm.factory.DefaultPcmFactory;

/**
 * Created by gbecan on 13/10/14.
 */
public class KMFJSONExporter implements PCMExporter {

    @Override
    public String export(PCM pcm) {
        return toJson(pcm);
    }

    private DefaultPcmFactory factory = new DefaultPcmFactory();

    public String toJson(PCM pcm) {
        String json = "";

        if (pcm instanceof PCMImpl) {
            pcm.PCM kPcm = ((PCMImpl) pcm).getKpcm();

            JSONModelSerializer serializer = factory.createJSONSerializer();
            json = serializer.serialize(kPcm);
        }

        return json;
    }
}
