package misc.hints;

import org.opencompare.api.java.*;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.*;

import java.util.List;

/**
 * Created by gbecan on 02/02/15.
 */
public class MyPCMPrinter implements PCMVisitor {

    private boolean isBooleanCell;

    /**
     * Print some information contained in a PCM
     * @param pcm: PCM to print
     */
    public void print(PCM pcm) {

        // We start by listing the names of the products
        System.out.println("--- Products ---");
        for (Product product : pcm.getProducts()) {
            System.out.println(product.getKeyContent());
        }

        // Then, we use a visitor to print the content of the cells that represent a boolean value
       // System.out.println("--- Boolean values ---");
        pcm.accept(this);

    }


    // Methods for the visitor

    @Override
    public void visit(PCM pcm) {
        for (Product product : pcm.getProducts()) {
            product.accept(this);
        }
    }

    @Override
    public void visit(Feature feature) {

    }

    @Override
    public void visit(FeatureGroup featureGroup) {

    }

    @Override
    public void visit(Product product) {
        for (Cell cell : product.getCells()) {
            cell.accept(this);
        }
    }

    @Override
    public void visit(Cell cell) {
        Value interpretation = cell.getInterpretation();

        // Visit the interpretation of the cell to check if it is a boolean
        isBooleanCell = false;
        if (interpretation != null) {
            interpretation.accept(this);
        }

        // Print content of the cell if it is a boolean
        if (isBooleanCell) {
            System.out.println(cell.getContent());
        }
    }

    @Override
    public void visit(BooleanValue booleanValue) {
        isBooleanCell = true;
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
        System.out.println("Integer " + integerValue.getValue());
    }

    @Override
    public void visit(Multiple multiple) {
        List<Value> subs = multiple.getSubValues();
        System.out.println("(begin multiple)");
        for (Value v : subs)
            v.accept(this);
        System.out.println("(end multiple)");
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
        System.out.println("REAL " + realValue.getValue());
    }

    @Override
    public void visit(StringValue stringValue) {
        System.out.println("String: " + stringValue.getValue());
    }

    @Override
    public void visit(Unit unit) {

    }

    @Override
    public void visit(Version version) {

    }

    @Override
    public void visit(Image image) {
        System.out.println("\tIMAGE " + image.getUrl());
    }


}
