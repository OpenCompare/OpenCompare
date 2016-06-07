package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.NotAvailable;

/**
 * Created by gbecan on 14/10/14.
 */
public class NotAvailableImpl extends ValueImpl implements NotAvailable {

    private org.opencompare.model.NotAvailable kNotAvailable;

    public NotAvailableImpl(org.opencompare.model.NotAvailable kNotAvailable) {
        super(kNotAvailable);
        this.kNotAvailable = kNotAvailable;
    }

    public org.opencompare.model.NotAvailable getkNotAvailable() {
        return kNotAvailable;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        NotAvailable copy = factory.createNotAvailable();
        return copy;
    }
}
