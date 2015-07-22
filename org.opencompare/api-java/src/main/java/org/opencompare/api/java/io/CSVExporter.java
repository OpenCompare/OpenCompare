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

    private PCMMetadata metadata = null;
    private StringWriter stringWriter;
    private CSVWriter csvWriter;
    private List<String> headerLine;
    private List<String> productLine;
    private char separator = ',';
    private char quote = '"';
    private List<String[]> lines;
    private LinkedList<AbstractFeature> nextFeaturesToVisit;
    private int featureDepth;

    @Override
    public String export(PCMContainer container) {
        metadata = container.getMetadata();
        return export(container.getPcm());
    }

    private String export(PCM pcm) {
        // Compute depth
        featureDepth = pcm.getFeaturesDepth();
        // Export features
        if (metadata == null) {
            metadata = new PCMMetadata(pcm);
        }

        lines = new ArrayList<>();
        stringWriter = new StringWriter();
        csvWriter = new CSVWriter(stringWriter, separator, quote);
        pcm.accept(this);

        if (!metadata.getProductAsLines()) {
            IOMatrix matrix = new IOMatrix(lines);
            matrix.transpose();
            lines = matrix.toList();
        }

        csvWriter.writeAll(lines);

        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public String export(PCMContainer container, char separator, char quote) {
        this.separator = separator;
        this.quote = quote;
        return export(container);
    }

    @Override
    public void visit(PCM pcm) {

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
                    return metadata.getFeaturePosition(feat1) - metadata.getFeaturePosition(feat2);
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

        for (Product product : metadata.getSortedProducts()) {
            productLine = new ArrayList<>();
            product.accept(this);
            lines.add(productLine.toArray(new String[productLine.size()]));
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
        for (Feature feature : metadata.getSortedFeatures()) {
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
