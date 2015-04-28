package org.opencompare.api.java.value;

import org.opencompare.api.java.Value;

/**
 * Created by gbecan on 09/10/14.
 */
public interface RealValue extends Value {
    double getValue();
    void setValue(double value);
}
