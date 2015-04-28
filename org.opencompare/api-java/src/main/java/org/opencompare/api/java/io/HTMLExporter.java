package org.opencompare.api.java.io;

import org.opencompare.api.java.*;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.*;

import java.util.*;

/**
 * Created by gbecan on 13/10/14.
 */
public class HTMLExporter implements PCMVisitor, PCMExporter {

    private StringBuilder builder;

    private LinkedList<AbstractFeature> nextFeaturesToVisit;
    private boolean computeFeatureDepth;
    private int featureDepth;
    private Map<Feature, Integer> featurePosition;
    private int nextFeaturePosition;

    @Override
    public String export(PCM pcm) {
        return toHTML(pcm);
    }

    public String toHTML(PCM pcm) {
        builder = new StringBuilder();
        pcm.accept(this);
        return builder.toString();

//        String html = "";
//
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        try {
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document document = builder.newDocument();
//            document.createElement("html");
//
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer transformer = tf.newTransformer();
//        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//        StringWriter writer = new StringWriter();
//        transformer.transform(new DOMSource(doc), new StreamResult(writer));
//        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
//        return html;
    }

    @Override
    public void visit(PCM pcm) {
        builder.append("<html>");
        builder.append(" <head>\n" +
                "    \t\t<meta charset=\"utf-8\"/>\n" +
                "    </head>");
        builder.append("<body>");

        builder.append("<h1>");
        builder.append(pcm.getName());
        builder.append("</h1>");

        builder.append("<table border=\"1\">");

        // Compute depth
        computeFeatureDepth = true;
        featureDepth = 1;
        nextFeaturePosition = 0;
        featurePosition = new HashMap<Feature, Integer>();

        for (AbstractFeature feature : pcm.getFeatures()) {
            feature.accept(this);
        }

        // Generate HTML code for features
        computeFeatureDepth = false;
        LinkedList<AbstractFeature> featuresToVisit;
        featuresToVisit = new LinkedList<AbstractFeature>();
        nextFeaturesToVisit = new LinkedList<AbstractFeature>();
        featuresToVisit.addAll(pcm.getFeatures());

        while(!featuresToVisit.isEmpty()) {
            builder.append("<tr>");
            builder.append("<th></th>");
            for (AbstractFeature feature : featuresToVisit) {
                feature.accept(this);
            }

            featuresToVisit = nextFeaturesToVisit;
            nextFeaturesToVisit = new LinkedList<AbstractFeature>();
            builder.append("</tr>\n");
            featureDepth--;
        }

        // Generate HTML code for products
        for (Product product : pcm.getProducts()) {
            product.accept(this);
        }

        builder.append("<table>");

        builder.append("</body>");
        builder.append("</html>");
    }

    @Override
    public void visit(Feature feature) {
        if (computeFeatureDepth) {
            featurePosition.put(feature, nextFeaturePosition);
            nextFeaturePosition++;
        } else {
            builder.append("<th");
            if (featureDepth > 1) {
                builder.append(" rowspan=" + featureDepth + " ");
            }
            builder.append(">" + feature.getName() + "</th>");
        }

    }

    @Override
    public void visit(FeatureGroup featureGroup) {
        if (computeFeatureDepth) {

            featureDepth++;
            for (AbstractFeature subFeature : featureGroup.getFeatures()) {
                subFeature.accept(this);
            }

        } else {

            builder.append("<th");
            if (!featureGroup.getFeatures().isEmpty()) {
                builder.append(" colspan=" + featureGroup.getFeatures().size() + " ");
            }
            builder.append(">");
            builder.append(featureGroup.getName());
            builder.append("</th>");

            nextFeaturesToVisit.addAll(featureGroup.getFeatures());
        }

    }

    @Override
    public void visit(Product product) {
        builder.append("<tr>");

        builder.append("<th>");
        builder.append(product.getName());
        builder.append("</th>");

        List<Cell> cells = product.getCells();

        Collections.sort(cells, new Comparator<Cell>() {
            @Override
            public int compare(Cell cell1, Cell cell2) {
                return featurePosition.get(cell1.getFeature()) - featurePosition.get(cell2.getFeature());
            }
        });

        for (Cell cell : cells) {
            builder.append("<td>");

            // Convert interpretation
            builder.append("<span title=\"");
            builder.append(cell.getInterpretation());
            builder.append("\">");

            // Convert content
            builder.append(cell.getContent());

            builder.append("</span>");
            builder.append("</td>");
        }

        builder.append("</tr>\n");
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
