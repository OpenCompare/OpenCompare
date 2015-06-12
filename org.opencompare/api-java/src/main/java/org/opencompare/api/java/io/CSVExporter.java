package org.opencompare.api.java.io;

import com.opencsv.CSVWriter;
import javafx.collections.transformation.SortedList;
import org.opencompare.api.java.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by gbecan on 3/19/15.
 */
public class CSVExporter implements PCMExporter {

    PCMMetadata currentMetadata = null;

    @Override
    public String export(PCMContainer container) {
        currentMetadata = container.getMetadata();
        return export(container.getPcm());
    }

    @Override
    public String export(PCM pcm) {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        // Export features
        if (currentMetadata  == null) {
            currentMetadata = new PCMMetadata(pcm);
        }

        List<Feature> features = currentMetadata.getSortedFeatures();
        List<Product> products = currentMetadata.getSortedProducts();
        List<String> featureLine = new ArrayList<String>();

        featureLine.add("Product");

        for (Feature feature : features) {
            featureLine.add(feature.getName());
        }

        csvWriter.writeNext(featureLine.toArray(new String[featureLine.size()]));

        // Export products
        for (Product product : products) {
            List<String> productLine = new ArrayList<String>();

            productLine.add(product.getName());
            for (Feature feature : features) {
                Cell cell = product.findCell(feature);
                if (cell == null) {
                    productLine.add("");
                } else {
                    productLine.add(cell.getContent());
                }

            }

            csvWriter.writeNext(productLine.toArray(new String[productLine.size()]));
        }


        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringWriter.toString();
    }
}
