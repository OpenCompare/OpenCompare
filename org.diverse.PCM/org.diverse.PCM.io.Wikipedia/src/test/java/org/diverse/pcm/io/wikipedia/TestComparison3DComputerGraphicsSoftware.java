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
 * Created by baqu on 01/12/14.
 */
public class TestComparison3DComputerGraphicsSoftware {
    /* Our pcm file, convert to String for our Jsoup parser */
    String file;

    /* The doc, allowing us to parse the pcm table */
    Document doc;

    @Before
    public void setUp(){
        try {
            file = readFile("output/model/Comparison_of_3D_computer_graphics_software.pcm", Charset.defaultCharset());
            doc = Jsoup.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* Test on the title line */
    @Test
    public void TestTitleComparison3DComputerGraphics(){

            /* The List we're supposed to get */
        ArrayList<String> list = new ArrayList<String>() {{
            add("Application");
            add("Latest release date and version");
            add("Developed by");
            add("Platforms");
            add("Mainly Used For");
            add("License");
        }};
        assertEquals(list, getTitleLine(doc));
    }

    /* Test on a column */
    @Test
    public void TestColumnComparison3DComputerGraphics(){

        /* The list we're supposed to get */
        ArrayList<String> list = new ArrayList<String>() {{
            add("Autodesk");
            add("Dmitry Trofimov");
            add("Inivis");
            add("Afanche Technologies, Inc.");
            add("Peter Eastman");
            add("Blender Foundation");
            add("DAZ 3D");
            add("DAZ 3D");
            add("Dr. Martin Wengenmayer");
            add("MAXON");
            add("Procedural");
            add("Exocortex");
            add("Ashlar-Vellum");
            add("E-on Software");
            add("EIAS3D");
            add("autodessys, Inc.");
            add("DAZ 3D");
            add("Side Effects Software");
            add("Reallusion");
            add("KUBOTEK");
            add("NewTek");
            add("Massive Software");
            add("Autodesk");
            add("O. Mizno");
            add("Bentley Systems");
            add("The Foundry");
            add("Triple Squid Software Design");
            add("Autodesk");
            add("The POV-Team");
            add("Parametric Technology Corporation");
            add("Remograph");
            add("McNeel");
            add("Pixologic");
            add("Shade3D");
            add("Nevercenter");
            add("Trimble Navigation");
            add("Autodesk");
            add("Siemens PLM Software");
            add("solidThinking");
            add("Dassault systems");
            add("SpaceClaim Corporation");
            add("Electric Rain");
            add("Missler Software");
            add("Caligari Corporation");
            add("Michael L. Farrell");
            add("ViewPoint 3D");
            add("Dan Gudmundsson (maintainer)");
            add("Pixologic");
        }};
        assertEquals(list, getColumnNumber(2, doc));

        assertEquals("Dmitry Trofimov", getCell(2, 2, doc));
    }

    /* Test on a line */
    @Test
    public void TestLineComparison3DComputerGraphics(){

        /* The list we're supposed to get */
        ArrayList<String> list = new ArrayList<String>() {{
            add("AC3D");
            add("2014-03-03 v 7.2.17");
            add("Linux, Mac OS X, Microsoft Windows");
            add("Modeling");
            add("Proprietary");
        }};
        assertEquals(list, getLineNumber(3, doc));
    }

    /* Test on a Cell */
    @Test
    public void TestCell1Comparison3DComputerGraphics(){
        assertEquals("Java Virtual Machine supported platforms", getCell(5, 3, doc));
    }

    /* Test on a Cell */
    @Test
    public void TestCell2Comparison3DComputerGraphics(){
        assertEquals("Cheetah 3D", getCell(9, 0, doc));
    }
}

