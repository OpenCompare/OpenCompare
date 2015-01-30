package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.impl.ValueImpl;
import org.diverse.pcm.api.java.util.PCMVisitor;
import org.diverse.pcm.api.java.value.Unit;
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
}
