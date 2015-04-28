package org.opencompare.api.java.util;

import org.opencompare.api.java.*;
import org.opencompare.api.java.value.*;

/**
 * Created by gbecan on 13/10/14.
 */
public interface PCMVisitor {

    void visit(PCM pcm);
    void visit(Feature feature);
    void visit(FeatureGroup featureGroup);
    void visit(Product product);
    void visit(Cell cell);
    void visit(BooleanValue booleanValue);
    void visit(Conditional conditional);
    void visit(DateValue dateValue);
    void visit(Dimension dimension);
    void visit(IntegerValue integerValue);
    void visit(Multiple multiple);
    void visit(NotApplicable notApplicable);
    void visit(NotAvailable notAvailable);
    void visit(Partial partial);
    void visit(RealValue realValue);
    void visit(StringValue stringValue);
    void visit(Unit unit);
    void visit(Version version);


}
