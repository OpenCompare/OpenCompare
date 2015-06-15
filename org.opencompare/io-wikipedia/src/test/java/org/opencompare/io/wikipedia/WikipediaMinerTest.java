package org.opencompare.io.wikipedia;

import org.junit.Test;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.io.wikipedia.export.CSVExporter;
import org.opencompare.io.wikipedia.export.HTMLExporter;
import org.opencompare.io.wikipedia.export.PCMModelExporter;
import org.opencompare.io.wikipedia.io.WikiTextExporter;
import org.opencompare.io.wikipedia.io.WikiTextLoader;
import org.opencompare.io.wikipedia.pcm.Page;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static scala.collection.JavaConversions.seqAsJavaList;

/**
 * Created by gbecan on 19/11/14.
 */
public class WikipediaMinerTest {

    @Test
    public void test() throws IOException {
        WikiTextLoader miner = new WikiTextLoader();

        // Parse article from Wikipedia
        String title = "Comparison_of_AMD_processors";
        String code = miner.getPageCodeFromWikipedia(title);
        Page page = miner.mineInternalRepresentation(code, title);


        // HTML export
        HTMLExporter htmlExporter = new HTMLExporter();
        String html = htmlExporter.export(page);
        assertNotNull(html);

        // CSV export
        CSVExporter csvExporter = new CSVExporter();
        String csv = csvExporter.export(page);
        assertNotNull(csv);

        // PCM model export
        PCMModelExporter pcmExporter = new PCMModelExporter();
        List<PCMContainer> containers = pcmExporter.export(page);
        assertFalse(containers.isEmpty());

        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)
        WikiTextExporter wikitextExporter = new WikiTextExporter();
        for (PCMContainer container : containers) {
            String wikitext = wikitextExporter.export(container);
            assertNotNull(wikitext);
        }

    }

}
