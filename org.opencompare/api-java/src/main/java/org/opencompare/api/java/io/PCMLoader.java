package org.opencompare.api.java.io;

import org.opencompare.api.java.PCM;

import java.io.File;
import java.io.IOException;

/**
 * Created by gbecan on 30/01/15.
 */
public interface PCMLoader {

    /**
     * Load a PCM from its string representation
     * @param pcm : string representation of a PCM
     * @return the PCM represented by pcm
     */
    PCM load(String pcm);

    /**
     * Load a PCM from a file
     * @param file
     * @return
     * @throws java.io.FileNotFoundException
     */
    PCM load(File file) throws IOException;
}
