package org.opencompare.api.java.impl.io;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMMetadata;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.io.PCMExporter;
import org.kevoree.modeling.api.json.JSONModelSerializer;
import pcm.factory.DefaultPcmFactory;

import java.util.Base64;

/**
 * Created by gbecan on 13/10/14.
 */
public class KMFJSONExporter implements PCMExporter {

    private DefaultPcmFactory factory = new DefaultPcmFactory();
    private JSONModelSerializer serializer = factory.createJSONSerializer();
    private PCMBase64Encoder encoder = new PCMBase64Encoder();
    private PCMBase64Decoder decoder = new PCMBase64Decoder();

    @Override
    public String export(PCMContainer container) {
        return toJson(container.getPcm());
    }

    public String toJson(PCM pcm) {
        String json = "";

        if (pcm instanceof PCMImpl) {
            // Convert all strings to base64 to avoid encoding problems
            encoder.encode(pcm);

            // Serialize PCM
            pcm.PCM kPcm = ((PCMImpl) pcm).getKpcm();
            json = serializer.serialize(kPcm);

            // Decode PCM
            decoder.decode(pcm);
        }

        return json;
    }

}
