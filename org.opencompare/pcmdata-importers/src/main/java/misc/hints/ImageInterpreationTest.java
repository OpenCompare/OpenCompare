package misc.hints;

import misc.PCMUtil;
import org.junit.Test;
import org.opencompare.api.java.*;
import org.opencompare.api.java.extractor.CellContentInterpreter;
import org.opencompare.api.java.impl.PCMFactoryImpl;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by macher1 on 14/04/2017.
 */
public class ImageInterpreationTest {


    @Test
    public void testPCM1() throws IOException {

        // Load a PCM
        File pcmFile = new File("input-pcm/eras2.pcm");
        PCMLoader loader = new KMFJSONLoader();
        PCM pcm = loader.load(pcmFile).get(0).getPcm();
        assertNotNull(pcm);

        /*
        String csvFileName = "input-csv/erasURL.csv";
        List<PCMContainer> pcms = PCMUtil.loadCSV(csvFileName); // already interpreted with CellContentInterpreter
        PCMContainer pcmContainer = pcms.get(0);
        assertNotNull(pcmContainer);

        PCM pcm = pcmContainer.getPcm();
        assertNotNull(pcm);*/


        PCMFactory pcmFactory = new PCMFactoryImpl();
        CellContentInterpreter cellContentInterpreter = new CellContentInterpreter(pcmFactory);

        pcm.normalize(pcmFactory);
        cellContentInterpreter.interpretCellsFromScratch(pcm);


        // Execute the printer
        MyPCMPrinter myPrinter = new MyPCMPrinter();
        myPrinter.print(pcm);


    }
}
