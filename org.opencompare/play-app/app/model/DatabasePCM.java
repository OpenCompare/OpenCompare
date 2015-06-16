package model;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMMetadata;

/**
 * Created by gbecan on 12/12/14.
 */
public class DatabasePCM {

    private String id;
    private PCMContainer pcmContainer;

    public DatabasePCM(String id, PCMContainer pcmContainer) {
        this.id = id;
        this.pcmContainer = pcmContainer;
    }

    public String getId() {
        return id;
    }

    public PCMContainer getPCMContainer() {
        return pcmContainer;
    }

    public void setPCMContainer(PCMContainer pcmContainer) {
        this.pcmContainer = pcmContainer;
    }

    public boolean hasIdentifier() {
        return this.id != null;
    }
}
