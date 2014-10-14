package org.diverse.pcm.api.java.impl.export;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.export.PCMtoJson;
import org.diverse.pcm.api.java.impl.PCMImpl;
import org.kevoree.modeling.api.json.JSONModelSerializer;
import pcm.factory.DefaultPcmFactory;

/**
 * Created by gbecan on 13/10/14.
 */
public class PCMtoJsonImpl implements PCMtoJson {

    private DefaultPcmFactory factory = new DefaultPcmFactory();

    @Override
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
