package org.opencompare.io.wikipedia;

import org.opencompare.api.java.PCM;
import org.opencompare.io.wikipedia.WikipediaPageMiner;
import org.opencompare.io.wikipedia.export.CSVExporter;
import org.opencompare.io.wikipedia.export.HTMLExporter;
import org.opencompare.io.wikipedia.export.PCMModelExporter;
import org.opencompare.io.wikipedia.export.WikiTextExporter;
import org.opencompare.io.wikipedia.pcm.Page;
import static org.junit.Assert.*;
import org.junit.Test;
import static scala.collection.JavaConversions.*;

import java.util.List;

/**
 * Created by gbecan on 19/11/14.
 */
public class WikipediaMinerTest {

    @Test
    public void test() {
        WikipediaPageMiner miner = new WikipediaPageMiner();

        // Parse article from Wikipedia
        String title = "Comparison_of_Nikon_DSLR_cameras";
        String code = miner.getPageCodeFromWikipedia(title);
        String preprocessedCode = miner.preprocess(code);
        Page page = miner.parse(preprocessedCode, title);


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
        List<PCM> pcms = seqAsJavaList(pcmExporter.export(page));
        assertFalse(pcms.isEmpty());

        // Transform a list of PCM models into wikitext (markdown language for Wikipedia articles)
        WikiTextExporter wikitextExporter = new WikiTextExporter();
        for (PCM pcm : pcms) {
            String wikitext = wikitextExporter.toWikiText(pcm);
            assertNotNull(wikitext);
        }

    }

}
