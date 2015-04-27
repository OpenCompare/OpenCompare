package org.diverse.pcm.api.java.io;

import com.opencsv.CSVWriter;
import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Feature;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.Product;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbecan on 3/19/15.
 */
public class CSVExporter implements PCMExporter {

    @Override
    public String export(PCM pcm) {

        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        // Export features
        List<Feature> features = pcm.getConcreteFeatures(); // FIXME : does not support feature groups
        List<String> featureLine = new ArrayList<String>();

        featureLine.add("Product");

        for (Feature feature : features) {
            featureLine.add(feature.getName());
        }

        csvWriter.writeNext(featureLine.toArray(new String[featureLine.size()]));

        // Export products
        for (Product product : pcm.getProducts()) {
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
