package org.opencompare.api.java.io;

import com.opencsv.CSVWriter;
import org.opencompare.api.java.*;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by gbecan on 3/19/15.
 */
public class CSVExporter implements PCMExporter {

    @Override
    public String export(PCMContainer container) {
        return export(container, ',', '"');

    }

    public String export(PCMContainer container, char separator, char quote) {

        PCM pcm = container.getPcm();
        PCMMetadata metadata = container.getMetadata();
        if (metadata == null) {
            metadata = new PCMMetadata(pcm);
        }

        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter, separator, quote);

        if (metadata.getProductAsLines()) {
            exportProductsAsLines(pcm, metadata, csvWriter);
        } else {
            exportFeaturesAsLines(pcm, metadata, csvWriter);
        }

        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();

    }

    public void exportProductsAsLines(PCM pcm, PCMMetadata metadata, CSVWriter csvWriter) {
        String[] line = new String[pcm.getConcreteFeatures().size()];

        // Convert features
        List<List<AbstractFeature>> flattenHierarchy = metadata.getFlattenFeatureHierarchy();
        for (List<AbstractFeature> level : flattenHierarchy) {
            int index = 0;
            for (AbstractFeature feature : level) {
                line[index] = feature.getName();
                index++;
            }
            csvWriter.writeNext(line);
        }

        // Convert products
        for (Product product : metadata.getSortedProducts()) {
            int index = 0;
            for (Feature feature : metadata.getSortedFeatures()) {
                Cell cell = product.findCell(feature);
                if (cell == null) {
                    line[index] = "";
                } else {
                    line[index] = cell.getContent();
                }
                index++;
            }
            csvWriter.writeNext(line);
        }
    }

    public void exportFeaturesAsLines(PCM pcm, PCMMetadata metadata, CSVWriter csvWriter) {
        String[] line = new String[pcm.getProducts().size() + pcm.getFeaturesDepth()];

        List<List<AbstractFeature>> flattenHierarchy = metadata.getFlattenFeatureHierarchy();
        List<Feature> sortedFeatures = metadata.getSortedFeatures();

        for (int i = 0; i < pcm.getConcreteFeatures().size(); i++) {
            int index = 0;

            // Convert features
            for (List<AbstractFeature> level : flattenHierarchy) {
                AbstractFeature feature = level.get(i);
                line[index] = feature.getName();
                index++;
            }

            Feature feature = sortedFeatures.get(i);
            List<Cell> cells = feature.getCells();
            Collections.sort(cells, (c1, c2) -> Integer.compare(metadata.getProductPosition(c1.getProduct()), metadata.getProductPosition(c2.getProduct())));

            // Convert products
            for (Cell cell : cells) {
                line[index] = cell.getContent();
                index++;
            }

            csvWriter.writeNext(line);
        }


    }
}
