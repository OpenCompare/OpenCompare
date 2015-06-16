package org.opencompare.api.java.impl.io;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMMetadata;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.io.PCMExporter;
import org.kevoree.modeling.api.json.JSONModelSerializer;
import pcm.factory.DefaultPcmFactory;

/**
 * Created by gbecan on 13/10/14.
 */
public class KMFJSONExporter implements PCMExporter {

    private DefaultPcmFactory factory = new DefaultPcmFactory();

    @Override
    public String export(PCMContainer container) {
        return toJson(container.getPcm());
    }

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
