package org.opencompare.api.java;

import org.opencompare.api.java.util.PCMVisitor;

/**
 * Created by gbecan on 13/10/14.
 */
public interface PCMElement {

    void accept(PCMVisitor visitor);
    PCMElement clone(PCMFactory factory);

}
