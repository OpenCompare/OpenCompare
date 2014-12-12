package model;

import org.diverse.pcm.api.java.PCM;

/**
 * Created by gbecan on 12/12/14.
 */
public class PCMVariable {

    private String id;
    private PCM pcm;


    public PCMVariable(String id, PCM pcm) {
        this.id = id;
        this.pcm = pcm;
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
}
