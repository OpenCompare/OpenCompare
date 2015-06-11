package model;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMMetadata;

/**
 * Created by gbecan on 12/12/14.
 */
public class PCMVariable {

    private String id;
    private PCM pcm;
    private PCMMetadata metadata;


    public PCMVariable(String id, PCM pcm, PCMMetadata metadata) {
        this.id = id;
        this.pcm = pcm;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
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

    public boolean hasIdentifier() {
        return this.id != null;
    }
}
