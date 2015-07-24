package org.opencompare.api.java.util;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.io.IOCell;

/**
 * Created by smangin on 7/9/15.
 */
public class MatrixComparatorEqualityImpl implements MatrixComparator {

    @Override
    public boolean compareCells(IOCell c1, IOCell c2) {
        if (c1 == null && c2 == null) {
            return true;
        }
        if (c1 == null) {
            return false;
        }
        return c1.equals(c2);
    }
}
