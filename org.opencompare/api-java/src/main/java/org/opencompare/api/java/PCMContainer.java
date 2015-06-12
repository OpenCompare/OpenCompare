package org.opencompare.api.java;

/**
 * Created by gbecan on 12/12/14.
 */
public class PCMContainer {

    private PCM pcm;
    private PCMMetadata metadata;

    public PCMContainer() {}

    public PCMContainer(PCM pcm, PCMMetadata metadata) {
        this.pcm = pcm;
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
