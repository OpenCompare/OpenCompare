package org.diverse.pcm.api.java.io;

import org.diverse.pcm.api.java.PCM;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by gbecan on 12/12/14.
 */
public interface JSONLoader {

    /**
     * Load a PCM from a string representing its JSON representation
     * @param json : json representation of a PCM
     * @return
     */
    PCM load(String json);

    /**
     * Load a PCM from a file in JSON format
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    PCM load(File file) throws FileNotFoundException;

}
