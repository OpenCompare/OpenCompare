package org.diverse.pcm.api.java;

import java.util.List;

public interface Feature extends AbstractFeature

{


    /**
     * This  method return us the value of cell of feature
     * @param
     * @return
     */
    List<Cell> getListFeature(PCM product);
}
