import model.Database;
import org.junit.Ignore;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.formalizer.extractor.CellContentInterpreter;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by gbecan on 12/12/14.
 */
public class LoadPCMs {

    @Test
    public void testLoadWikipediaPCMs() throws IOException {
        String path = "../dataset-wikipedia/output/formalized/model";
        File dir = new File(path);

        KMFJSONLoader loader = new KMFJSONLoader();
        CellContentInterpreter interpreter = new CellContentInterpreter();

        for (File file : dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".pcm");
            }
        })) {
            PCM pcm = loader.load(file);
            pcm.setName(pcm.getName().replaceAll("_", " "));

            if (pcm.isValid()) {
                interpreter.interpretCells(pcm);
                Database.INSTANCE.save(pcm);
            }
        }

    }

}
