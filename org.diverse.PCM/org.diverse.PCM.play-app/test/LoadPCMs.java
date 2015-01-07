import model.Database;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.impl.io.JSONLoaderImpl;
import org.diverse.pcm.api.java.io.JSONLoader;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

/**
 * Created by gbecan on 12/12/14.
 */
public class LoadPCMs {
    @Test
    public void testLoadWikipediaPCMs() throws FileNotFoundException {
        String path = "../org.diverse.PCM.io.Wikipedia/output/model";
        File dir = new File(path);

        JSONLoader loader = new JSONLoaderImpl();

        for (File file : dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".pcm");
            }
        })) {
            PCM pcm = loader.load(file);
            Database.INSTANCE.save(pcm);
        }
    }
}
