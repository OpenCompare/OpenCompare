package org.diverse.pcm.api.java.io;

import com.opencsv.CSVReader;
import org.diverse.pcm.api.java.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by gbecan on 4/2/15.
 */
public class CSVLoader implements PCMLoader {

    private PCMFactory factory;

    public CSVLoader(PCMFactory factory) {
        this.factory = factory;
    }

    @Override
    public PCM load(String pcm) {
        CSVReader reader = new CSVReader(new StringReader(pcm));
        PCM loadedPCM = null;
        try {
            loadedPCM = load(reader);
            reader.close();
        } catch (IOException e) {

        }
        return loadedPCM;
    }

    @Override
    public PCM load(File file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        PCM pcm = load(reader);
        reader.close();

        return pcm;
    }

    private PCM load(CSVReader reader) throws IOException {
        PCM pcm = factory.createPCM();

        // Features
        String[] featureNames = reader.readNext();
        ArrayList<Feature> features = new ArrayList<Feature>();
        for (int i = 1; i < featureNames.length; i++) {
            String featureName = featureNames[i];
            Feature feature = factory.createFeature();
            feature.setName(featureName);
            pcm.addFeature(feature);
            features.add(feature);
        }


        String[] line = reader.readNext();
        while (line != null) {
            // Products
            Product product = factory.createProduct();
            product.setName(line[0]);
            pcm.addProduct(product);

            // Cells
            for (int i = 1; i < line.length; i++) {
                Cell cell = factory.createCell();
                cell.setContent(line[i]);
                cell.setFeature(features.get(i));
                product.addCell(cell);
            }

            line = reader.readNext();
        }

        return pcm;
    }
}

