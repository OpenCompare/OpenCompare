package org.opencompare.api.java.io;

import com.opencsv.CSVWriter;
import org.opencompare.api.java.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by gbecan on 3/19/15.
 */
public class CSVExporter implements PCMExporter {

    PCMMetadata currentMetadata = null;
    char separator = ',';
    char quote = '"';

    public CSVExporter setSeparator(char separator) {
        this.separator = separator;
        return this;
    }

    public CSVExporter setQuote(char quote) {
        this.quote = quote;
        return this;
    }

    @Override
    public String export(PCMContainer container) {
        currentMetadata = container.getMetadata();
        return export(container.getPcm());
    }

    private List<String[]> exportProductAsLines(List<Product> products, List<Feature> features) {
        List<String> headerLine = new ArrayList<>();
        List<String[]> lines = new ArrayList<>();

        headerLine.add("Product");
        for (Feature feature : features) {
            headerLine.add(feature.getName());
        }
        lines.add(headerLine.toArray(new String[headerLine.size()]));
        for (Product product : products) {
            List<String> productLine = new ArrayList<>();

            productLine.add(product.getName());
            for (Feature feature : features) {
                Cell cell = product.findCell(feature);
                if (cell == null) {
                    productLine.add("");
                } else {
                    productLine.add(cell.getContent());
                }

            }

            lines.add(productLine.toArray(new String[productLine.size()]));
        }
        return lines;
    }

    private List<String[]> exportFeatureAsLines(List<Product> products, List<Feature> features) {
        List<String> headerLine = new ArrayList<>();
        List<String[]> lines = new ArrayList<>();

        headerLine.add("Feature");
        for (Product product : products) {
            headerLine.add(product.getName());
        }
        lines.add(headerLine.toArray(new String[headerLine.size()]));
        for (Feature feature : features) {

            List<String> featureLine = new ArrayList<>();

            featureLine.add(feature.getName());
            for (Product product : products) {
                Cell cell = product.findCell(feature);
                if (cell == null) {
                    featureLine.add("");
                } else {
                    featureLine.add(cell.getContent());
                }

            }

            lines.add(featureLine.toArray(new String[featureLine.size()]));
        }
        return lines;
    }

    private String export(PCM pcm) {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter, separator, quote);

        // Export features
        if (currentMetadata  == null) currentMetadata = new PCMMetadata(pcm);

        List<Feature> features = currentMetadata.getSortedFeatures();
        List<Product> products = currentMetadata.getSortedProducts();
        List<String[]> lines;

        if (currentMetadata.getProductAsLines()) {
            lines = exportProductAsLines(products, features);
        } else {
            lines = exportFeatureAsLines(products, features);
        }

        csvWriter.writeAll(lines);

        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
}
