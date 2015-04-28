package org.opencompare.api.java.value;

import org.opencompare.api.java.Value;

/**
 * Created by gbecan on 09/10/14.
 */
public interface IntegerValue extends Value {
    int getValue();
    void setValue(int value);
}
