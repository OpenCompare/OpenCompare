package org.opencompare.api.java.io;

import com.opencsv.CSVReader;
import org.opencompare.api.java.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    public List<PCMContainer> load(String pcm) {
        List<PCMContainer> containers = new ArrayList<>();
        try {
            containers = load(new StringReader(pcm));
        } catch (IOException e) {

        }
        return containers;
    }

    @Override
    public List<PCMContainer> load(File file) throws IOException {
        List<PCMContainer> containers = new ArrayList<>();
        try {
            containers = load(new FileReader(file));
        } catch (IOException e) {

        }
        return containers;
    }

    public List<PCMContainer> load(Reader reader) throws IOException {
        CSVReader csvReader = new CSVReader(reader, separator, quote);
        List<PCMContainer> containers = new ArrayList<>();
        if (productsAsLines) {
            containers.add(loadFeatureFirst(csvReader));
        } else {
            containers.add(loadProductFirst(csvReader));
        }
        csvReader.close();
        return containers;
    }

    private PCMContainer loadFeatureFirst(CSVReader reader) throws IOException {
        PCM pcm = factory.createPCM();
        PCMMetadata metadata = new PCMMetadata(pcm);
        metadata.setProductAsLines(this.productsAsLines);
        PCMContainer container = new PCMContainer(metadata);

        // Features
        String[] featureNames = reader.readNext();

        if (featureNames != null) {
            ArrayList<Feature> features = new ArrayList<Feature>();
            for (int i = 1; i < featureNames.length; i++) {
                String featureName = featureNames[i];
                Feature feature = factory.createFeature();
                feature.setName(featureName);
                pcm.addFeature(feature);
                features.add(feature);
                // And keep the order in metadata
                metadata.setFeaturePosition(feature, i);
            }


            String[] line = reader.readNext();
            int index = 0; // Metadata index
            while (line != null) {
                // Products
                Product product = factory.createProduct();
                product.setName(line[0]);
                pcm.addProduct(product);
                // And keep the order in metadata
                metadata.setProductPosition(product, index);

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
                        metadata.setFeaturePosition(newFeature, i);
                    }

                    cell.setFeature(features.get(i - 1));

                    product.addCell(cell);
                }

                line = reader.readNext();
                index += 1;
            }
        }

        return container;
    }

    private PCMContainer loadProductFirst(CSVReader reader) throws IOException {
        PCM pcm = factory.createPCM();
        PCMMetadata metadata = new PCMMetadata(pcm);
        metadata.setProductAsLines(this.productsAsLines);
        PCMContainer container = new PCMContainer(metadata);

        // Products
        String[] productNames = reader.readNext();

        if (productNames != null) {
            ArrayList<Product> products = new ArrayList<Product>();
            for (int i = 1; i < productNames.length; i++) {
                String productName = productNames[i];
                Product product = factory.createProduct();
                product.setName(productName);
                pcm.addProduct(product);
                products.add(product);
                // And keep the order in metadata
                metadata.setProductPosition(product, i);
            }


            String[] line = reader.readNext();
            int index = 0; // Metadata index
            while (line != null) {
                // Features
                Feature feature = factory.createFeature();
                feature.setName(line[0]);
                pcm.addFeature(feature);
                // And keep the order in metadata
                metadata.setFeaturePosition(feature, index);

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
                        metadata.setProductPosition(newProduct, i);
                    }

                    products.get(i - 1).addCell(cell);
                }

                line = reader.readNext();
            }
        }



        return container;
    }
}

