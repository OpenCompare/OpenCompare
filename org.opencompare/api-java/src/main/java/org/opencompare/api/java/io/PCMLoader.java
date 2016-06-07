package org.opencompare.api.java.io;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMMetadata;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by gbecan on 30/01/15.
 */
public interface PCMLoader {

    /**
     * Return a list of PCM container from a string representation
     * @param pcm : string representation of a PCM
     * @return the PCM represented by pcm
     */
    List<PCMContainer> load(String pcm);

    /**
     * Return a PCM container from a file
     * @param file file to load
     * @return loaded PCM
     */
    List<PCMContainer> load(File file) throws IOException;
}
