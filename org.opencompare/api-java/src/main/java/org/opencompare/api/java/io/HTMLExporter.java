package org.opencompare.api.java.io;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.opencompare.api.java.*;
import org.opencompare.api.java.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gbecan on 13/10/14.
 */
public class HTMLExporter implements PCMExporter {

    @Override
    public String export(PCMContainer pcmContainer) {
        PCM pcm = pcmContainer.getPcm();

        final PCMMetadata metadata;
        if (pcmContainer.getMetadata() == null) {
            metadata = new PCMMetadata(pcm);
        } else {
            metadata = pcmContainer.getMetadata();
        }

        // Create HTML document
        Document doc = Document.createShell("");
        Element head = doc.head();
        Element body = doc.body();

        // Meta info
        head.appendElement("meta")
        .attr("charset", "utf-8");

        // Create title
        head.appendElement("title").text(pcm.getName());
        body.appendElement("h1").text(pcm.getName());

        // Create table
        Element table = body.appendElement("table")
                .attr("border", "1");

        // Generate HTML code for features
        exportFeatures(metadata, table);

        // Generate HTML code for products
        exportProducts(pcm, metadata, table);


        // Export to HTML code
        Document.OutputSettings settings = new Document.OutputSettings().prettyPrint(true);
        return doc.outputSettings(settings).outerHtml();

    }

    private void exportFeatures(PCMMetadata metadata, Element table) {
        List<Pair<AbstractFeature, Pair<Integer, Integer>>> currentFeatureLevel = new ArrayList<>();

        for (Feature feature : metadata.getSortedFeatures()) {
            currentFeatureLevel.add(new Pair<>(feature, new Pair<>(0, 1)));
        }

        List<Element> lines = new ArrayList<>();
        boolean noParents = false;

        while(!currentFeatureLevel.isEmpty() && !noParents) {

            List<Pair<AbstractFeature, Pair<Integer, Integer>>> nextFeatureLevel = new ArrayList<>();
            List<Pair<AbstractFeature, Pair<Integer, Integer>>> row = new ArrayList<>();

            // Analyze hierarchy of features
            noParents = true;
            int i = 0;
            while (i < currentFeatureLevel.size()) {
                Pair<AbstractFeature, Pair<Integer, Integer>> data = currentFeatureLevel.get(i);
                AbstractFeature aFeature = data._1;
                Pair<Integer, Integer> span = data._2;

                // Compute colspan
                int colspan = 1;
                while (i > 0 && aFeature.equals(currentFeatureLevel.get(i - 1)._1)) {
                    i++;
                    colspan++;
                }

                // Compute rowspan and prepare for next iteration
                AbstractFeature parentGroup = aFeature.getParentGroup();
                if (parentGroup == null) {
                    int rowspan = span._1 + 1;
                    nextFeatureLevel.add(new Pair<>(aFeature, new Pair<>(rowspan, colspan)));
                } else {
                    row.add(new Pair<>(aFeature, new Pair<>(span._1, colspan)));
                    noParents = false;
                }
                i++;
            }


            if (noParents) {
                row = nextFeatureLevel;
                nextFeatureLevel = new ArrayList<>();
            }

            // Create HTML elements
            Element line = new Element(Tag.valueOf("tr"), "");
            if (noParents) {
                for (Pair<AbstractFeature, Pair<Integer, Integer>> data : row) {
                    AbstractFeature aFeature = data._1;
                    Pair<Integer, Integer> span = data._2;

                    Element th = line.appendElement("th");
                    th.text(aFeature.getName());

                    if (span._1 > 1) {
                        th.attr("rowspan", span._1.toString());
                    }

                    if (span._2 > 1) {
                        th.attr("colspan", span._2.toString());
                    }

                }
            }
            lines.add(line);

            currentFeatureLevel = nextFeatureLevel;
        }

    // Add rows to table
        Collections.reverse(lines);
        for (Element line : lines) {
            table.appendChild(line);
        }
    }

    private void exportProducts(PCM pcm, PCMMetadata metadata, Element table) {
        for (Product product : metadata.getSortedProducts()) {
            Element tr = table.appendElement("tr");

            for (Feature feature : metadata.getSortedFeatures()) {
                Cell cell = product.findCell(feature);

                Element htmlCell;
                if (pcm.getProductsKey().equals(feature)) {
                    htmlCell = tr.appendElement("th");
                } else {
                    htmlCell = tr.appendElement("td");
                }

                htmlCell.html(cell.getContent().replaceAll("\n", "<br />"));
            }
        }
    }

}
