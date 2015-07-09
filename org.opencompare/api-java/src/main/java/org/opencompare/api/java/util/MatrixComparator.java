package org.opencompare.api.java.util;

import org.opencompare.api.java.AbstractFeature;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.io.IOCell;

import java.util.List;

/**
 * Created by smangin on 7/9/15.
 */
public interface MatrixComparator {

    boolean compareCells(IOCell c1, IOCell c2);

}
