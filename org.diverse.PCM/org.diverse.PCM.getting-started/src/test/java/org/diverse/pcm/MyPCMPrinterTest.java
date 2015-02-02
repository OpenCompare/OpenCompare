package org.diverse.pcm;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.impl.io.KMFJSONLoader;
import static org.junit.Assert.*;

import org.diverse.pcm.api.java.io.PCMLoader;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by gbecan on 02/02/15.
 */
public class MyPCMPrinterTest {

    @Test
    public void testMyPCMPrinter() throws FileNotFoundException, URISyntaxException {

        // Define a file containing a PCM
        URL url = getClass().getResource("/example.pcm");
        File pcmFile = new File(url.toURI());
        assertTrue(pcmFile.exists());

        // Load a PCM
        PCMLoader loader = new KMFJSONLoader();
        PCM pcm = loader.load(pcmFile);
        assertNotNull(pcm);

        // Execute the printer
        MyPCMPrinter myPrinter = new MyPCMPrinter();
        myPrinter.print(pcm);


    }

}
