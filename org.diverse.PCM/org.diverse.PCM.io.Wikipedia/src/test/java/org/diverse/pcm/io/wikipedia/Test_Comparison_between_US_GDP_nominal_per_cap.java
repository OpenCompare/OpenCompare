package org.diverse.pcm.io.wikipedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.diverse.pcm.io.wikipedia.FileFunctions.readFile;
import static org.diverse.pcm.io.wikipedia.ReaderPCM.getCell;
import static org.diverse.pcm.io.wikipedia.ReaderPCM.getTitleLine;
import static org.junit.Assert.assertEquals;

/**
 * Created by yoannlt on 22/12/14.
 */
public class Test_Comparison_between_US_GDP_nominal_per_cap {


    /* Our pcm file, convert to String for our Jsoup parser */
    String file;

    /* The doc, allowing us to parse the pcm table */
    Document doc;

    @Before
    public void setUp(){
        try {
            file = readFile("output/model/Comparison_between_U.S._states_and_countries_by_GDP_(nominal)_per_capita.pcm", Charset.defaultCharset());
            doc = Jsoup.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestComparaisonTitleLine(){
        /* The List we're supposed to get */
        List<String> list = new ArrayList<String>() {{
            add("Overall rank");
            add("Rank by country/Rank by US State");
            add("Country/US State");
            add("GDP per capita (USD)");
        }};
        assertEquals(list, getTitleLine(doc));
    }

    @Test
    public void TestComparaisonCell(){
        assertEquals("Luxembourg", getCell(2, 5, doc));
        assertEquals("Ireland", getCell(2, 10, doc));
    }

}
