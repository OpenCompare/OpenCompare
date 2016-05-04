package org.opencompare.api.java.io;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.PCMMetadata;

/**
 * Created by gbecan on 30/01/15.
 */
public interface PCMExporter {

    /**
     * Export PCM from a PCM container
     * @param container container to export
     * @return string representing the PCM
     */
    String export(PCMContainer container);

}
