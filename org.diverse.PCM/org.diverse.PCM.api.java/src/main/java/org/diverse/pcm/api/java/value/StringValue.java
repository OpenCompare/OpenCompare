package org.diverse.pcm.api.java.value;

import org.diverse.pcm.api.java.Value;

/**
 * Created by gbecan on 09/10/14.
 */
public interface StringValue extends Value {
    String getValue();
    void setValue(String value);
}
