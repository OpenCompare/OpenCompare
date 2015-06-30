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

    public void encode(PCM pcm) {
        pcm.accept(this);
    }

    @Override
    public void visit(PCM pcm) {

        String name = pcm.getName();
        String encodedName = new String(encoder.encode(name.getBytes()));
        pcm.setName(encodedName);

    }

    @Override
    public void visit(Feature feature) {

    }

    @Override
    public void visit(FeatureGroup featureGroup) {

    }

    @Override
    public void visit(Product product) {

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
