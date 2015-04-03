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
    private char separator;
    private char quote;
    private boolean productsAsLines;

    public CSVLoader(PCMFactory factory) {
        this(factory, ',', '"', true);
    }

    public CSVLoader(PCMFactory factory, char separator) {
        this(factory, separator, '"', true);
    }

    public CSVLoader(PCMFactory factory, char separator, char quote) {
        this(factory, separator, quote, true);
    }

    public CSVLoader(PCMFactory factory, boolean productsAsLines) {
        this(factory, ',', '"', productsAsLines);
    }

    public CSVLoader(PCMFactory factory, char separator, char quote, boolean productsAsLines) {
        this.factory = factory;
        this.separator = separator;
        this.quote = quote;
        this.productsAsLines = productsAsLines;
    }

    @Override
    public PCM load(String pcm) {
        CSVReader reader = new CSVReader(new StringReader(pcm), separator, quote);
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
        CSVReader reader = new CSVReader(new FileReader(file), separator, quote);
        PCM pcm = load(reader);
        reader.close();

        return pcm;
    }

    private PCM load(CSVReader reader) throws IOException {
        if (productsAsLines) {
            return loadFeatureFirst(reader);
        } else {
            return loadProductFirst(reader);
        }
    }

    private PCM loadFeatureFirst(CSVReader reader) throws IOException {
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
                cell.setFeature(features.get(i - 1));
                product.addCell(cell);
            }

            line = reader.readNext();
        }

        return pcm;
    }

    private PCM loadProductFirst(CSVReader reader) throws IOException {
        PCM pcm = factory.createPCM();

        // Products
        String[] productNames = reader.readNext();
        ArrayList<Product> products = new ArrayList<Product>();
        for (int i = 1; i < productNames.length; i++) {
            String productName = productNames[i];
            Product product = factory.createProduct();
            product.setName(productName);
            pcm.addProduct(product);
            products.add(product);
        }


        String[] line = reader.readNext();
        while (line != null) {
            // Features
            Feature feature = factory.createFeature();
            feature.setName(line[0]);
            pcm.addFeature(feature);

            // Cells
            for (int i = 1; i < line.length; i++) {
                Cell cell = factory.createCell();
                cell.setContent(line[i]);
                cell.setFeature(feature);
                products.get(i - 1).addCell(cell);
            }

            line = reader.readNext();
        }

        return pcm;
    }
}

