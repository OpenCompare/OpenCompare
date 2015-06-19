package org.opencompare.io.wikipedia;

import org.junit.Test;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.io.wikipedia.export.CSVExporter;
import org.opencompare.io.wikipedia.export.HTMLExporter;
import org.opencompare.io.wikipedia.export.PCMModelExporter;
import org.opencompare.io.wikipedia.io.MediaWikiAPI;
import org.opencompare.io.wikipedia.io.WikiTextExporter;
import org.opencompare.io.wikipedia.io.WikiTextLoader;
import org.opencompare.io.wikipedia.io.WikiTextTemplateProcessor;
import org.opencompare.io.wikipedia.pcm.Page;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
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
        String wikipediaURL = "wikipedia.org";
        MediaWikiAPI api = new MediaWikiAPI(wikipediaURL);
        WikiTextLoader miner = new WikiTextLoader(new WikiTextTemplateProcessor(api));


        // Parse article from Wikipedia
        String title = "Comparison_of_Nikon_DSLR_cameras";
        String language = "en";
        String code = api.getWikitextFromTitle(language, title);
        Page page = miner.mineInternalRepresentation(language, code, title);

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
        System.out.println(page.getMatrices().size());
        assertFalse(containers.isEmpty());

        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)
        WikiTextExporter wikitextExporter = new WikiTextExporter();
        for (PCMContainer container : containers) {
            String wikitext = wikitextExporter.export(container);
            assertNotNull(wikitext);
        }

    }

}
