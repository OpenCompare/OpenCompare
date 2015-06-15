package org.opencompare.api.java;

import org.opencompare.api.java.util.ComplexePCMElementComparator;
import org.opencompare.api.java.util.DiffResult;
import org.opencompare.api.java.util.PCMElementComparator;

/**
 * Created by gbecan on 12/12/14.
 */
public class PCMContainer {

    private PCM pcm;
    private PCMMetadata metadata;

    public PCMContainer() {}

    public PCMContainer(PCMMetadata metadata) {
        this.pcm = metadata.pcm;
        this.metadata = metadata;
    }

    public PCM getPcm() {
        return pcm;
    }

    public void setPcm(PCM pcm) {
        this.pcm = pcm;
    }

    public PCMMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PCMMetadata metadata) {
        this.metadata = metadata;
    }

}
