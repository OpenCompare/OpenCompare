package org.opencompare.api.java.io;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.opencompare.api.java.*;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.*;

import java.util.*;

/**
 * Created by gbecan on 13/10/14.
 */
public class HTMLExporter implements PCMVisitor, PCMExporter {

    private Document doc;
    private Element matrix;
    private PCMMetadata metadata;
    private Element tr; // Current column
    Document.OutputSettings settings = new Document.OutputSettings();
    private String templateMinimal = "<table id=\"matrix\" border=\"1\">\n" +
            "</table>";
    private String templateFull = "<html>\n" +
            "\t<head>\n" +
            "\t\t<meta charset=\"utf-8\"/>\n" +
            "\t\t<title></title>\n" +
            "\t</head>\n" +
            "\t<body>\n" +
            "\t\t<h1 id=\"title\"></h1>\n" +
            "\t\t<table id=\"matrix\" border=\"1\">\n" +
            "\t\t</table>\n" +
            "\t</body>\n" +
            "</html>";

    private LinkedList<AbstractFeature> nextFeaturesToVisit;
    private boolean computeFeatureDepth;
    private int featureDepth;

    @Override
    public String export(PCMContainer container) {
        return toHTML(container);
    }

    public String toHTML(PCM pcm) {
        settings.prettyPrint();
        doc = Jsoup.parse(templateFull);
        if (metadata == null) {
            metadata = new PCMMetadata(pcm);
        }
        pcm.accept(this);
        return doc.outputSettings(settings).outerHtml();

    }

    public String toHTML(PCMContainer container) {
        metadata = container.getMetadata();
        return toHTML(container.getPcm());
    }

    @Override
    public void visit(PCM pcm) {
        doc.head().select("title").first().text(pcm.getName());
        doc.body().select("h1").first().text(pcm.getName());
        matrix = doc.body().select("table").first();

        // Compute depth
        featureDepth = pcm.getFeaturesDepth();

        // Generate HTML code for features
        LinkedList<AbstractFeature> featuresToVisit;
        featuresToVisit = new LinkedList<>();
        nextFeaturesToVisit = new LinkedList<>();
        featuresToVisit.addAll(pcm.getFeatures());

        Collections.sort(featuresToVisit, new Comparator<AbstractFeature>() {
            @Override
            public int compare(AbstractFeature feat1, AbstractFeature feat2) {
                return metadata.getFeaturePosition(feat1) - metadata.getFeaturePosition(feat2);
            }
        });

        while(!featuresToVisit.isEmpty()) {
            tr = matrix.appendElement("tr");
            for (AbstractFeature feature : featuresToVisit) {
                feature.accept(this);
            }
            featuresToVisit = nextFeaturesToVisit;
            nextFeaturesToVisit = new LinkedList<>();
            featureDepth--;
        }

        // Generate HTML code for products
        for (Product product : pcm.getProducts()) {
            tr = matrix.appendElement("tr");
            product.accept(this);
        }
    }

    @Override
    public void visit(Feature feature) {
        Element th = tr.appendElement("th");
        if (featureDepth > 1) {
            th.attr("rowspan", Integer.toString(featureDepth));
        }
        th.text(feature.getName());
    }

    @Override
    public void visit(FeatureGroup featureGroup) {
        Element th = tr.appendElement("th");
        if (!featureGroup.getFeatures().isEmpty()) {
            th.attr("colspan", Integer.toString(featureGroup.getFeatures().size()));
        }
        th.text(featureGroup.getName());
        nextFeaturesToVisit.addAll(featureGroup.getFeatures());
    }

    @Override
    public void visit(Product product) {
        tr.appendElement("th").text(product.getName());

        List<Cell> cells = product.getCells();

        Collections.sort(cells, new Comparator<Cell>() {
            @Override
            public int compare(Cell cell1, Cell cell2) {
                return metadata.getSortedFeatures().indexOf(cell1.getFeature()) - metadata.getSortedFeatures().indexOf(cell2.getFeature());
            }
        });

        for (Cell cell : cells) {
            Element td = tr.appendElement("td");
            td.appendElement("span").text(cell.getContent());
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
