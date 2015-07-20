package org.opencompare.api.java.io;

import com.opencsv.CSVReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.opencompare.api.java.*;
import org.opencompare.api.java.util.MatrixAnalyser;
import org.opencompare.api.java.util.MatrixComparatorEqualityImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gbecan on 4/2/15.
 */
public class HTMLLoader implements PCMLoader {

    private PCMFactory factory;
    private boolean productsAsLines;
    private Map<Integer, Feature> features;

    public HTMLLoader(PCMFactory factory) {
        this(factory, true);
    }

    public HTMLLoader(PCMFactory factory, boolean productsAsLines) {
        this.factory = factory;
        this.productsAsLines = productsAsLines;
    }

    public static List<IOMatrix> createMatrices(Document doc) {
        List<IOMatrix> matrices = new ArrayList<>();
        String pageName = doc.head().getElementsByTag("title").text();
        int indice = 0;
        for (Element table: doc.getElementsByTag("table")) {
            IOMatrix matrix = new IOMatrix().setName(pageName + " #" + indice);
            int i = 0;
            for (Element line: table.getElementsByTag("tr")) {
                int j = 0;

                for (Element column: line.getAllElements()) {
                    if (column.tag().getName().equals("th") || column.tag().getName().equals("td")){
                        matrix.setCell(new IOCell(column.text()), i, j, 1, 1);
                        j++;
                    }
                }
                i++;
            }
            matrices.add(matrix);
            System.out.println(matrix.toString());
            indice++;
        }
        return matrices;
    }

    @Override
    public List<PCMContainer> load(String pcm) {
        List<PCMContainer> containers = new ArrayList<>();
        containers = load(createMatrices(Jsoup.parse(pcm)).get(0));
        return containers;
    }

    @Override
    public List<PCMContainer> load(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        List<PCMContainer> containers = new ArrayList<>();
        for (IOMatrix matrix: createMatrices(doc)) {
            containers.add(load(matrix).get(0));
        }
        return containers;
    }

    public List<PCMContainer> load(IOMatrix matrix) {
        return new IOMatrixLoader(this.factory, this.productsAsLines).load(matrix);
    }

}

