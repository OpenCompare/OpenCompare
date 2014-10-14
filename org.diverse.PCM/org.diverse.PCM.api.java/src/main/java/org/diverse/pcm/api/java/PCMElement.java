package org.diverse.pcm.api.java;

import org.diverse.pcm.api.java.util.PCMVisitor;

/**
 * Created by gbecan on 13/10/14.
 */
public interface PCMElement {

    void accept(PCMVisitor visitor);

}
