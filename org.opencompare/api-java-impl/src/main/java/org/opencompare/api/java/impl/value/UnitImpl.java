package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.Unit;
import pcm.Value;

/**
 * Created by gbecan on 30/01/15.
 */
public class UnitImpl extends ValueImpl implements Unit {

    private pcm.Unit kUnit;

    public UnitImpl(pcm.Unit kUnit) {
        super(kUnit);
        this.kUnit = kUnit;
    }

    public pcm.Unit getkUnit() {
        return kUnit;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        Unit copy = factory.createUnit();
        return copy;
    }
}
