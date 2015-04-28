package org.opencompare.api.java.io;

import org.opencompare.api.java.PCM;

/**
 * Created by gbecan on 30/01/15.
 */
public interface PCMExporter {

    /**
     * Export a PCM
     * @param pcm
     * @return
     */
    String export(PCM pcm);

}
