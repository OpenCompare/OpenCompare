package org.diverse.pcm.io.wikipedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.diverse.pcm.io.wikipedia.FileFunctions.*;
import static org.diverse.pcm.io.wikipedia.ReaderPCM.*;
import static org.junit.Assert.*;

/**
 * Created by yoannlt on 01/12/14.
 */
public class TestComparisonGrammar {
    /* Our pcm file, convert to String for our Jsoup parser */
    String file;

    /* The doc, allowing us to parse the pcm table */
    Document doc;

    @Before
    public void setUp(){
        try {
            file = readFile("output/model/Comparison_(grammar).pcm", Charset.defaultCharset());
            doc = Jsoup.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Test on the title line */
    @Test
    public void TestTitleComparisonGrammar(){

            /* The List we're supposed to get */
            ArrayList<String> list = new ArrayList<String>() {{
                add("Positive");
                add("Comparative");
                add("Superlative");
            }};
            assertEquals(list, getTitleLine(doc));
    }

    /* Test on a column */
    @Test
    public void TestColumnComparisonGrammar(){

        /* The list we're supposed to get */
        ArrayList<String> list = new ArrayList<String>() {{
            add("better");
            add("better");
            add("worse");
            add("worse");
            add("farther");
            add("further");
            add("smaller, less(er)");
            add("more");
        }};
        assertEquals(list, getColumnNumber(1, doc));

        assertEquals("worse", getCell(2, 3, doc));
    }

    /* Test on a line */
    @Test
    public void TestLineComparisonGrammar(){

        /* The list we're supposed to get */
        ArrayList<String> list = new ArrayList<String>() {{
            add("bad");
            add("worse");
            add("worst");
        }};
        assertEquals(list, getLineNumber(3, doc));
    }

    /* Test on a Cell */
    @Test
    public void TestCell1Comparison_grammar(){
            assertEquals("best", getCell(2, 2, doc));
    }

    /* Test on a Cell( */
    @Test
    public void TestCell2Comparison_grammar(){
        assertEquals("worse", getCell(4, 1, doc));
    }

}
