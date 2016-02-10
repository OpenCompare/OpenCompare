package org.opencompare.api.java.io;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.opencompare.api.java.*;
import org.opencompare.api.java.interpreter.CellContentInterpreter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbecan on 4/2/15.
 */
public class HTMLLoader implements PCMLoader {

    private PCMFactory factory;
    private ImportMatrixLoader importMatrixLoader;

    public HTMLLoader(PCMFactory factory, CellContentInterpreter cellContentInterpreter) {
        this(factory, cellContentInterpreter, PCMDirection.UNKNOWN);
    }

    public HTMLLoader(PCMFactory factory, CellContentInterpreter cellContentInterpreter, PCMDirection pcmDirection) {
        this.factory = factory;

        importMatrixLoader = new ImportMatrixLoader(this.factory, cellContentInterpreter, pcmDirection);
    }

    private List<ImportMatrix> createMatrices(Document doc) {
        List<ImportMatrix> matrices = new ArrayList<>();
        String pageName = doc.head().getElementsByTag("title").text();
        int index = 0;
        Elements tables = doc.getElementsByTag("table");
        for (Element table: tables) {
            ImportMatrix matrix = new ImportMatrix();
            if (tables.size() > 1) {
                matrix.setName(pageName + " #" + index);
            } else {
                matrix.setName(pageName);
            }

            int i = 0;
            for (Element row: table.getElementsByTag("tr")) {
                int j = 0;

                // Skip cells created with rowspans before
                while (matrix.isPositionOccupied(i, j)) {
                    j++;
                }

                // Parse row
                for (Element column: row.getAllElements()) {
                    if (column.tag().getName().equals("th") || column.tag().getName().equals("td")){
                        if (matrix.getCell(i, j) != null) {
                            j++;
                        }
                        int rowspan = 1;
                        int colspan = 1;
                        if (!column.attributes().get("rowspan").isEmpty()) {
                            rowspan = Integer.valueOf(column.attributes().get("rowspan"));
                        }
                        if (!column.attributes().get("colspan").isEmpty()) {
                            colspan = Integer.valueOf(column.attributes().get("colspan"));
                        }

                        ImportCell importCell = new ImportCell(cellToText(column), rowspan, colspan);
                        matrix.setCell(importCell, i, j);
                        j += colspan;
                    }
                }

                i++;
            }
            matrices.add(matrix);
            index++;
        }
        return matrices;
    }

    private String cellToText(Element element) {
        String text = "";
        for (Node node : element.childNodes()) {
            switch (node.nodeName()) {
                case "#text":
                    TextNode tn = (TextNode) node;
                    text += tn.getWholeText();
                    break;
                case "br":
                    text += "\n";
                    break;
                default:
            }

        }
        return text;
    }

    @Override
    public List<PCMContainer> load(String pcm) {
        Document doc = Jsoup.parse(pcm);
        List<PCMContainer> containers = new ArrayList<>();
        for (ImportMatrix matrix: createMatrices(doc)) {
            containers.add(load(matrix).get(0));
        }
        return containers;
    }

    @Override
    public List<PCMContainer> load(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        List<PCMContainer> containers = new ArrayList<>();
        for (ImportMatrix matrix: createMatrices(doc)) {
            containers.add(load(matrix).get(0));
        }
        return containers;
    }

    public List<PCMContainer> load(ImportMatrix matrix) {
        List<PCMContainer> result = new ArrayList<>();
        result.add(importMatrixLoader.load(matrix));
        return result;
    }

}

