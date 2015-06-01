package org.opencompare.api.java.io;

import com.opencsv.CSVReader;
import org.opencompare.api.java.*;

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
        PCM loadedPCM = null;
        try {
            loadedPCM = load(new StringReader(pcm));
        } catch (IOException e) {

        }
        return loadedPCM;
    }

    @Override
    public PCM load(File file) throws IOException {
        return load(new FileReader(file));
    }

    public PCM load(Reader reader) throws IOException {
        CSVReader csvReader = new CSVReader(reader, separator, quote);
        PCM pcm;
        if (productsAsLines) {
            pcm = loadFeatureFirst(csvReader);
        } else {
            pcm = loadProductFirst(csvReader);
        }
        csvReader.close();
        return pcm;
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

                // Create an arbitrary feature if the number of cells is greater than the number of features
                if (i > features.size()) {
                    Feature newFeature = factory.createFeature();
                    newFeature.setName("Feature");
                    pcm.addFeature(newFeature);
                    features.add(newFeature);
                }

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

                // Create an arbitrary product if the number of cells is greater than the number of products
                if (i > products.size()) {
                    Product newProduct = factory.createProduct();
                    newProduct.setName("Product");
                    pcm.addProduct(newProduct);
                    products.add(newProduct);
                }

                products.get(i - 1).addCell(cell);
            }

            line = reader.readNext();
        }

        return pcm;
    }
}

