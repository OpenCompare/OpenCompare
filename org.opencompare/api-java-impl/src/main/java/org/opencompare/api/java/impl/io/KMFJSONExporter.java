package org.opencompare.api.java.impl.io;

import org.kevoree.modeling.api.json.JSONModelSerializer;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.io.PCMExporter;
import org.opencompare.model.pcm.factory.DefaultPcmFactory;

/**
 * Created by gbecan on 13/10/14.
 */
public class KMFJSONExporter implements PCMExporter {

    private boolean base64Encoding;

    public KMFJSONExporter() {
        this(true);
    }

    public KMFJSONExporter(boolean base64Encoding) {
        this.base64Encoding = base64Encoding;
    }

    private DefaultPcmFactory factory = new DefaultPcmFactory();
    private JSONModelSerializer serializer = factory.createJSONSerializer();
    private PCMBase64Encoder encoder = new PCMBase64Encoder();

    @Override
    public String export(PCMContainer container) {
        String json = "";

        if (container.getPcm() instanceof PCMImpl) {

            PCMImpl pcm = (PCMImpl) container.getPcm();

            // Convert all strings to base64 to avoid encoding problems
            if (base64Encoding) {
                encoder.encode(pcm);
            }

            // Serialize PCM
            org.opencompare.model.PCM kPcm = pcm.getKpcm();
            json = serializer.serialize(kPcm);

            // Decode PCM
            if (base64Encoding) {
                encoder.decode(pcm);
            }
        }

        return json;
    }

}
