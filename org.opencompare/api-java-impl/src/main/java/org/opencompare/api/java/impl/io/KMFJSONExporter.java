package org.opencompare.api.java.impl.io;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.impl.PCMImpl;
import org.opencompare.api.java.io.PCMExporter;
import pcm.PcmModel;
import pcm.PcmUniverse;
import pcm.PcmView;

/**
 * Created by gbecan on 13/10/14.
 */
public class KMFJSONExporter implements PCMExporter {

    @Override
    public String export(PCM pcm) {
        return toJson(pcm);
    }

    public String toJson(PCM pcm) {
        PcmModel model = new PcmModel();
        model.connect();
        PcmUniverse universe = model.newUniverse();
        PcmView view = universe.time(0l);

        String json = "";

        if (pcm instanceof PCMImpl) {
            pcm.PCM kPcm = ((PCMImpl) pcm).getKpcm();

            try {
                json = view.json().save(kPcm).getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return json;
    }
}
