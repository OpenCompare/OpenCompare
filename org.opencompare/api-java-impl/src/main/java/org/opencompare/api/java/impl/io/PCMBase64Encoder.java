package org.opencompare.api.java.impl.io;

import org.opencompare.api.java.*;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.*;

import java.util.Base64;

/**
 * Created by gbecan on 6/30/15.
 */
public class PCMBase64Encoder implements PCMVisitor {

    private Base64.Encoder encoder = Base64.getEncoder();
    private Base64.Decoder decoder = Base64.getDecoder();

    private boolean encoding = true;

    public void encode(PCM pcm) {
        encoding = true;
        pcm.accept(this);
    }

    public void decode(PCM pcm) {
        encoding = false;
        pcm.accept(this);
    }

    private String encodeBase64(String str) {
        if (encoding) {
            return new String(encoder.encode(str.getBytes()));
        } else {
            return new String(decoder.decode(str.getBytes()));
        }

    }

    @Override
    public void visit(PCM pcm) {

        pcm.setName(encodeBase64(pcm.getName()));

        for (AbstractFeature feature : pcm.getFeatures()) {
            feature.accept(this);
        }

        for (Product product : pcm.getProducts()) {
            product.accept(this);
        }

    }

    @Override
    public void visit(Feature feature) {
        feature.setName(encodeBase64(feature.getName()));
    }

    @Override
    public void visit(FeatureGroup featureGroup) {

        featureGroup.setName(encodeBase64(featureGroup.getName()));

        for (AbstractFeature feature : featureGroup.getFeatures()) {
            feature.accept(this);
        }

    }

    @Override
    public void visit(Product product) {

        for (Cell cell : product.getCells()) {
            cell.accept(this);
        }
    }

    @Override
    public void visit(Cell cell) {
        cell.setContent(encodeBase64(cell.getContent()));
        cell.setRawContent(encodeBase64(cell.getRawContent()));

        if (cell.getInterpretation() != null) {
            cell.getInterpretation().accept(this);
        }
    }

    @Override
    public void visit(BooleanValue booleanValue) {

    }

    @Override
    public void visit(Conditional conditional) {
        conditional.getCondition().accept(this);
        conditional.getValue().accept(this);
    }

    @Override
    public void visit(DateValue dateValue) {
        dateValue.setValue(encodeBase64(dateValue.getValue()));
    }

    @Override
    public void visit(Dimension dimension) {

    }

    @Override
    public void visit(IntegerValue integerValue) {

    }

    @Override
    public void visit(Multiple multiple) {
        for (Value subValue : multiple.getSubValues()) {
            subValue.accept(this);
        }
    }

    @Override
    public void visit(NotApplicable notApplicable) {

    }

    @Override
    public void visit(NotAvailable notAvailable) {

    }

    @Override
    public void visit(Partial partial) {
        partial.getValue().accept(this);
    }

    @Override
    public void visit(RealValue realValue) {

    }

    @Override
    public void visit(StringValue stringValue) {
        stringValue.setValue(encodeBase64(stringValue.getValue()));
    }

    @Override
    public void visit(Unit unit) {

    }

    @Override
    public void visit(Version version) {

    }
}
