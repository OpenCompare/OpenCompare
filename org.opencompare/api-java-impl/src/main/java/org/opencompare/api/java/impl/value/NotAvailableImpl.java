package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.NotAvailable;

/**
 * Created by gbecan on 14/10/14.
 */
public class NotAvailableImpl extends ValueImpl implements NotAvailable {

    private pcm.NotAvailable kNotAvailable;

    public NotAvailableImpl(pcm.NotAvailable kNotAvailable) {
        super(kNotAvailable);
        this.kNotAvailable = kNotAvailable;
    }

    public pcm.NotAvailable getkNotAvailable() {
        return kNotAvailable;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }
}
