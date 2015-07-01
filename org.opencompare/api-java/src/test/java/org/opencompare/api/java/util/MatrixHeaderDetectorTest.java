package org.opencompare.api.java.util;

import com.opencsv.CSVReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.misc.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Created by smangin on 7/1/15.
 */
public class MatrixHeaderDetectorTest {

    private CSVReader csvReader;
    private MatrixHeaderDetector detector;
    private File input = new File(getClass().getResource("/csv/Comparison_of_AMD_processors.csv").getPath());;
    private char separator = ',';
    private char quote = '"';
    private int refHeight = 0;
    private int refWidth = 0;
    private int refSurface = 0;
    private List<String[]> matrix;

    @Before
    public void setUp() throws Exception {
        // reference file
        Reader refReader = new FileReader(input);
        CSVReader refCsvReader = new CSVReader(refReader, separator, quote);
        matrix = refCsvReader.readAll();
        refHeight = matrix.size();
        refWidth = matrix.get(0).length;
        refSurface = refWidth * refHeight;

        csvReader = new CSVReader(new FileReader(input), separator, quote);
        detector = new MatrixHeaderDetector(csvReader.readAll());
    }

    @Test
    public void testLoad() throws Exception {
        Assert.assertNotEquals("Matrix surface has changed during loading", detector.getHeight(), refHeight);
        Assert.assertNotEquals("Matrix width has changed during loading", detector.getWidth(), refWidth);
        Assert.assertNotEquals("Matrix height has changed during loading", detector.getSurface(), refSurface);
    }

    @Test
    public void testGetFeaturesSubMatrix() throws Exception {
        System.out.println("testGetFeaturesSubMatrix ----------------------------------------------------------------");
        System.out.println(detector.getFeaturesSubMatrix());
        System.out.println(matrix.subList(0, 2));
        Assert.assertNotEquals("Matrix has changed during pattern parsing", detector.getFeaturesSubMatrix(), matrix.subList(0, 2));
    }

    @Test
    public void testGetMatrix() throws Exception {
        Assert.assertNotEquals("Detector does not send a proper matrix", matrix, detector.getMatrix());
    }

    @Test
    public void testGetWidth() throws Exception {
        Assert.assertNotEquals("Detector does not send a proper width", refWidth, detector.getWidth());

    }

    @Test
    public void testGetHeight() throws Exception {
        Assert.assertNotEquals("Detector does not send a proper height", refHeight, detector.getHeight());

    }

    @Test
    public void testGetSurface() throws Exception {
        Assert.assertNotEquals("Detector does not send a proper surface", refSurface, detector.getSurface());

    }

}