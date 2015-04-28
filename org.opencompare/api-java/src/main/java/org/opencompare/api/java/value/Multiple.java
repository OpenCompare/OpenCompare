package org.opencompare.api.java.value;

import org.opencompare.api.java.Value;

import java.util.List;

/**
 * Created by gbecan on 09/10/14.
 */
public interface Multiple extends Value {

    List<Value> getSubValues();
    void addSubValue(Value value);
    void removeSubValue(Value value);
}
