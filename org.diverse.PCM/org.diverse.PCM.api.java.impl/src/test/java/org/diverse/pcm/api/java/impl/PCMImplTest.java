package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.PCMTest;

/**
 * Created by gbecan on 09/10/14.
 */
public class PCMImplTest extends PCMTest {

    @Override
    public void setUp() {
        factory = new PCMFactoryImpl();
    }
}
