package org.opencompare.api.java.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.copy;

/**
 * Created by smangin on 7/1/15.
 */
public class MatrixHeaderDetector {

    private List<String[]> matrix;
    private int n;
    private int m;
    private int i;
    private int j;
    private int alpha;
    private int beta;
    private int delta;

    public MatrixHeaderDetector(List<String[]> matrix) {
        resetMatrix();
        this.matrix = matrix;
        getMatrixSize();
    }

    private void resetMatrix() {
        matrix = new ArrayList<>();
        n = 0;
        m = 0;
        i = 0;
        j = 0;
        alpha = i + 1;
        beta = j + 1;
        delta = 0;
    }

    public List<String[]> getMatrix() {
        List<String[]> returnMatrix = matrix;
        copy(matrix, this.matrix);
        return matrix;
    }
    private void getMatrixSize() {
        n = matrix.size() - 1;
        m = matrix.get(0).length - 1;
    }

    private String get(int i, int j){
        return matrix.get(i)[j];
    }

    public int getWidth() {
        return m;
    }
    public int getHeight() {
        return n;
    }
    public int getSurface() {
        return n * m;
    }

    private boolean oneLineDetect() {
        int line = 1;
        while (line <= n) {
            if (get(0, j) != get(line, j)) {
                return false;
            }
            line++;
        }
        if (i > 0) i--;
        else if (i == 0) {
            i++;
            delta++;
        }
        return true;
    }

    private boolean oneColDetect() {
        int col = 1;
        while (col <= m) {
           if (get(i, 0) != get(i, col)) {
               return false;
           }
           col++;
        }
        j++;
        return true;
    }

    private boolean rowspanDetect() {
        boolean result = false;
        while (get(i, j) == get(alpha, j)) {
            result = true;
            if (alpha == n) break;
            alpha++;
            i = alpha - 1;
        }
        return result;
    }

    private boolean colspanDetect() {
        boolean result = false;
        while (get(i, j) == get(i, beta)) {
            result = true;
            if (beta == m) break;
            beta++;
            j = beta - 1;
            if (i != 0) {
                i++;
                j = 1;
            }
        }
        return result;
    }

    private boolean validPosition() {
        return alpha <= n && beta <= m;
    }

    public List<String[]> getFeaturesSubMatrix(){

        while (validPosition()) {
            //// Exception check
            // line in one colspan, go previous line or next line depending of i
            if (oneLineDetect()) continue;
            // column in one rowspan, go next column same line
            if (oneColDetect()) continue;

            // Colspan detection, go end of colspan and jump next line to avoid product double colspan
            if (rowspanDetect()) continue;
            // Rowspan detection, go end of rowspan and beginning of line
            if (colspanDetect()) continue;

            // Continue on the next column by default
            j++;
            // relative position save
            alpha = i + 1;
            beta = j + 1;
        }
        List<String[]> submatrix = matrix.subList(delta, i);
        return submatrix;
    }

}
