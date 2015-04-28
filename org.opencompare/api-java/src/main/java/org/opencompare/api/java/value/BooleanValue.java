package org.opencompare.api.java.value;

import org.opencompare.api.java.Value;

/**
 * Created by gbecan on 09/10/14.
 */
public interface BooleanValue extends Value {
    boolean getValue();
    void setValue(boolean value);
}
