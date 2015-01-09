package org.diverse.pcm.io.wikipedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.diverse.pcm.io.wikipedia.FileFunctions.readFile;
import static org.diverse.pcm.io.wikipedia.ReaderPCM.getCell;
import static org.diverse.pcm.io.wikipedia.ReaderPCM.getColumnNumber;
import static org.diverse.pcm.io.wikipedia.ReaderPCM.getTitleLine;
import static org.junit.Assert.assertEquals;

/**
 * Created by yoannlt on 22/12/14.
 */
public class Test_Comparison_IRA_accounts {

    /* Our pcm file, convert to String for our Jsoup parser */
    String file;

    /* The doc, allowing us to parse the pcm table */
    Document doc;

    @Before
    public void setUp(){
        try {
            file = readFile("output/model/Comparison_of_401(k)_and_IRA_accounts.pcm", Charset.defaultCharset());
            doc = Jsoup.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestComparaisonIRATitleLine(){
        /* The List we're supposed to get */
        List<String> list = new ArrayList<String>() {{
            add("Tax year 2013");
            add("(Traditional) 401(k)");
            add("Roth 401(k)");
            add("Traditional IRA");
            add("Roth IRA");
        }};
        assertEquals(list, getTitleLine(doc));
    }

    @Test
    public void TestComparaisonIRATitleColonne(){

        /* The list we're supposed to get */
        ArrayList<String> list = new ArrayList<String>() {{
            add("Tax benefit");
            add("Subjected taxes");
            add("Employer or Individual");
            add("Contribution Limits");
            add("Contribution notes");
            add("Matching Contributions");
            add("Deduction Limits");
            add("Distributions");
            add("Forced Distributions");
            add("Loans");
            add("Early Withdrawal");
            add("Home Down Payment");
            add("Education Expenses");
            add("Medical Expenses");
            add("Conversions and Rollovers");
            add("Changing Institutions");
            add("Beneficiaries");
            add("Protection");
        }};
        assertEquals(list, getColumnNumber(1,doc));
    }

    @Test
    public void TestComparaisonIRACell(){
        assertEquals("Contributions are post-tax. Qualified distributions are not taxable.", getCell(2, 2, doc));
        assertEquals("Matching contributions available from some employers.", getCell(7, 1, doc));
    }

}
