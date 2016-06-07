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

    private ExportMatrixExporter exportMatrixExporter = new ExportMatrixExporter();

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

        ExportMatrix exportMatrix = exportMatrixExporter.export(pcmContainer);

        for (int row = 0; row < exportMatrix.getNumberOfRows(); row++) {
            Element htmlRow = table.appendElement("tr");

            for (int column = 0; column < exportMatrix.getNumberOfColumns(); column++) {
                ExportCell exportCell = exportMatrix.getCell(row, column);

                if (exportCell != null) {
                    Element htmlCell;
                    if (exportCell.isFeature() || exportCell.isInProductsKeyColumn()) {
                        htmlCell = htmlRow.appendElement("th");
                    } else {
                        htmlCell = htmlRow.appendElement("td");
                    }

                    htmlCell.attr("style", "white-space: pre;");
                    htmlCell.text(textToHTML(exportCell.getContent()));

                    if (exportCell.getRowspan() > 1) {
                        htmlCell.attr("rowspan", "" + exportCell.getRowspan());
                    }

                    if (exportCell.getColspan() > 1) {
                        htmlCell.attr("colspan", "" + exportCell.getColspan());
                    }
                }

            }
        }

        // Export to HTML code
        Document.OutputSettings settings = new Document.OutputSettings().prettyPrint(false);
        return doc.outputSettings(settings).outerHtml();

    }

    private String textToHTML(String text) {
//        String formattedText = text
//                .replaceAll("\n", "<br />");
//        return formattedText;
        return text;
    }

}
