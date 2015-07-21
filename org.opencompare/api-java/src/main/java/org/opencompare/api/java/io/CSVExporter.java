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
public class CSVExporter implements PCMExporter, PCMVisitor {

    private PCMMetadata currentMetadata = null;
    private StringWriter stringWriter;
    private CSVWriter csvWriter;
    private List<String> headerLine;
    private List<String> productLine;
    char separator = ',';
    char quote = '"';
    private LinkedList<AbstractFeature> nextFeaturesToVisit;
    private int featureDepth;

    @Override
    public String export(PCMContainer container) {
        currentMetadata = container.getMetadata();
        return export(container.getPcm());
    }

    private String export(PCM pcm) {
        stringWriter = new StringWriter();
        csvWriter = new CSVWriter(stringWriter, separator, quote);
        pcm.accept(this);
        return stringWriter.toString();
    }

    public String export(PCMContainer container, char separator, char quote) {
        this.separator = separator;
        this.quote = quote;
        return export(container);
    }

    private List<String[]> exportProductAsLines(PCM pcm) {
        List<String[]> lines = new ArrayList<>();

        // Generate HTML code for features
        LinkedList<AbstractFeature> featuresToVisit;
        featuresToVisit = new LinkedList<>();
        nextFeaturesToVisit = new LinkedList<>();
        featuresToVisit.addAll(pcm.getFeatures());

        while(!featuresToVisit.isEmpty()) {
            headerLine = new ArrayList<>();
            headerLine.add("Product");
            Collections.sort(featuresToVisit, new Comparator<AbstractFeature>() {
                @Override
                public int compare(AbstractFeature feat1, AbstractFeature feat2) {
                    return currentMetadata.getFeaturePosition(feat1) - currentMetadata.getFeaturePosition(feat2);
                }
            });
            for (AbstractFeature feature : featuresToVisit) {
                feature.accept(this);
            }
            lines.add(headerLine.toArray(new String[headerLine.size()]));
            featuresToVisit = nextFeaturesToVisit;
            nextFeaturesToVisit = new LinkedList<>();
            featureDepth--;
        }

        for (Product product : currentMetadata.getSortedProducts()) {
            productLine = new ArrayList<>();
            product.accept(this);
            lines.add(productLine.toArray(new String[productLine.size()]));
        }
        return lines;
    }

    private List<String[]> exportFeatureAsLines(PCM pcm) {
        List<String> headerLine = new ArrayList<>();
        List<String[]> lines = new ArrayList<>();

        headerLine.add("Feature");
        for (Product product : currentMetadata.getSortedProducts()) {
            headerLine.add(product.getName());
        }
        lines.add(headerLine.toArray(new String[headerLine.size()]));
        for (AbstractFeature feature : currentMetadata.getSortedFeatures()) {

            List<String> featureLine = new ArrayList<>();

            featureLine.add(feature.getName());
            for (Product product : pcm.getProducts()) {
                Cell cell = product.findCell((Feature) feature);
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

    @Override
    public void visit(PCM pcm) {

        // Compute depth
        featureDepth = pcm.getFeaturesDepth();
        // Export features
        if (currentMetadata  == null) {
            currentMetadata = new PCMMetadata(pcm);
        }

        List<String[]> lines;
        if (currentMetadata.getProductAsLines()) {
            lines = exportProductAsLines(pcm);
        } else {
            lines = exportFeatureAsLines(pcm);
        }

        csvWriter.writeAll(lines);

        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(Feature feature) {
        headerLine.add(feature.getName());
        if (featureDepth > 1) {
            nextFeaturesToVisit.add(feature);
        }
    }

    @Override
    public void visit(FeatureGroup featureGroup) {
        for (int i = 0; i < featureGroup.getFeatures().size(); i++) {
            headerLine.add(featureGroup.getName());
        }
        nextFeaturesToVisit.addAll(featureGroup.getFeatures());
    }

    @Override
    public void visit(Product product) {
        productLine.add(product.getName());
        for (Feature feature : currentMetadata.getSortedFeatures()) {
            Cell cell = product.findCell(feature);
            if (cell == null) {
                productLine.add("");
            } else {
                productLine.add(cell.getContent());
            }

        }
    }

    @Override
    public void visit(Cell cell) {

    }

    @Override
    public void visit(BooleanValue booleanValue) {

    }

    @Override
    public void visit(Conditional conditional) {

    }

    @Override
    public void visit(DateValue dateValue) {

    }

    @Override
    public void visit(Dimension dimension) {

    }

    @Override
    public void visit(IntegerValue integerValue) {

    }

    @Override
    public void visit(Multiple multiple) {

    }

    @Override
    public void visit(NotApplicable notApplicable) {

    }

    @Override
    public void visit(NotAvailable notAvailable) {

    }

    @Override
    public void visit(Partial partial) {

    }

    @Override
    public void visit(RealValue realValue) {

    }

    @Override
    public void visit(StringValue stringValue) {

    }

    @Override
    public void visit(Unit unit) {

    }

    @Override
    public void visit(Version version) {

    }
}
