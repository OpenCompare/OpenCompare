package org.opencompare.api.java.interpreter;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Value;

/**
 * Created by gbecan on 10/02/16.
 */
public interface CellContentInterpreter {

    void interpretCells(PCM pcm);

    Value interpretString(String content);

}
