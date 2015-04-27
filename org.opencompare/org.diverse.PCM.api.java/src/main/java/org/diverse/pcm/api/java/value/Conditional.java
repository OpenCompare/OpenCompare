package org.diverse.pcm.api.java.value;

import org.diverse.pcm.api.java.Value;

/**
 * Created by gbecan on 09/10/14.
 */
public interface Conditional extends Value {

    Value getValue();
    void setValue(Value value);

    Value getCondition();
    void setCondition(Value condition);
}
