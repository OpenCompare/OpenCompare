package org.diverse.pcm.io.wikipedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.diverse.pcm.io.wikipedia.FileFunctions.readFile;
import static org.diverse.pcm.io.wikipedia.ReaderPCM.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by yoannlt on 01/12/14.
 */
public class TestComparisonBetweenArgentineProvinces {
    /* Our pcm file, convert to String for our Jsoup parser */
    String file;

    /* The doc, allowing us to parse the pcm table */
    Document doc;

    @Before
    public void setUp(){
        try {
            file = readFile("output/model/Comparison_between_Argentine_provinces_and_countries_by_GDP_(PPP)_per_capita.pcm", Charset.defaultCharset());
            doc = Jsoup.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Test on the title line */
    @Test
    public void TestTitleComparisonBetweenArgentineProvinces(){

            /* The List we're supposed to get */
            ArrayList<String> list = new ArrayList<String>() {{
                add("Rank");
                add("Country and (Argentine provinces)");
                add("Intl. $");
            }};
            assertEquals(list, getTitleLine(doc));
    }

    /* Test on a line */
    @Test
    public void TestLineComparisonBetweenArgentineProvinces(){

        /* The list we're supposed to get */
        ArrayList<String> list = new ArrayList<String>() {{
            add("4");
            add("Singapore");
            add("51,226");
        }};
        assertEquals(list, getLineNumber(3, doc));
    }

    /* Test on a Cell */
    @Test
    public void TestCell1ComparisonBetweenArgentineProvinces(){
            assertEquals("53,738", getCell(2, 3, doc));
    }

    /* Test on a Cell */
    @Test
    public void TestCell2ComparisonBetweenArgentineProvinces(){
        assertEquals("Brunei", getCell(5, 1, doc));
    }

}
